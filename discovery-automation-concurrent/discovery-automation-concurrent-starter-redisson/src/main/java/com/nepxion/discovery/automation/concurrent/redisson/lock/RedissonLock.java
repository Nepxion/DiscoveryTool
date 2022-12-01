package com.nepxion.discovery.automation.concurrent.redisson.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.automation.concurrent.redisson.constant.RedissonConstant;

public class RedissonLock {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonLock.class);

    private RedissonClient redissonClient;

    // 可重入锁可重复使用, 不需要考虑锁删除
    private volatile Map<String, RLock> unfairLockMap;
    private volatile Map<String, RLock> fairLockMap;
    private volatile Map<String, RLock> readLockMap;
    private volatile Map<String, RLock> writeLockMap;
    private volatile Map<RedissonLockType, List<Long>> lockThreadIdMap;

    public RedissonLock(Config config) {
        this(Redisson.create(config));
    }

    public RedissonLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.unfairLockMap = new ConcurrentHashMap<String, RLock>();
        this.fairLockMap = new ConcurrentHashMap<String, RLock>();
        this.readLockMap = new ConcurrentHashMap<String, RLock>();
        this.writeLockMap = new ConcurrentHashMap<String, RLock>();
        this.lockThreadIdMap = new ConcurrentHashMap<RedissonLockType, List<Long>>();
        this.lockThreadIdMap.put(RedissonLockType.UNFAIR, new CopyOnWriteArrayList<Long>());
        this.lockThreadIdMap.put(RedissonLockType.FAIR, new CopyOnWriteArrayList<Long>());
        this.lockThreadIdMap.put(RedissonLockType.READ, new CopyOnWriteArrayList<Long>());
        this.lockThreadIdMap.put(RedissonLockType.WRITE, new CopyOnWriteArrayList<Long>());
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public boolean tryLock(RedissonLockType lockType, String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        if (!isStarted()) {
            return false;
        }

        RLock lock = getLock(lockType, key);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (acquired) {
                addCurrentThreadId(lockType);
            }

            return acquired;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(RedissonLockType lockType, String key) {
        if (!isStarted()) {
            return;
        }

        RLock lock = getLock(lockType, key);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();

            removeCurrentThreadId(lockType);
        }
    }

    public RLock getLock(RedissonLockType lockType, String key) {
        switch (lockType) {
            case UNFAIR:
                return getCachedUnfairLock(key);
            case FAIR:
                return getCachedFairLock(key);
            case READ:
                return getCachedReadLock(key);
            case WRITE:
                return getCachedWriteLock(key);
        }

        return null;
    }

    private RLock getCachedUnfairLock(String key) {
        RLock lock = unfairLockMap.get(key);
        if (lock == null) {
            RLock newLock = getNewUnfairLock(key + "-" + RedissonConstant.UNFAIR);
            lock = unfairLockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RLock getCachedFairLock(String key) {
        RLock lock = fairLockMap.get(key);
        if (lock == null) {
            RLock newLock = getNewFairLock(key + "-" + RedissonConstant.FAIR);
            lock = fairLockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RLock getCachedReadLock(String key) {
        RLock lock = readLockMap.get(key);
        if (lock == null) {
            RLock newLock = getNewReadLock(key + "-" + RedissonConstant.READ_WRITE);
            lock = readLockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RLock getCachedWriteLock(String key) {
        RLock lock = writeLockMap.get(key);
        if (lock == null) {
            RLock newLock = getNewWriteLock(key + "-" + RedissonConstant.READ_WRITE);
            lock = writeLockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RLock getNewUnfairLock(String key) {
        return redissonClient.getLock(key);
    }

    private RLock getNewFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    private RLock getNewReadLock(String key) {
        return getNewReadWriteLock(key).readLock();
    }

    private RLock getNewWriteLock(String key) {
        return getNewReadWriteLock(key).writeLock();
    }

    private RReadWriteLock getNewReadWriteLock(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);

        return readWriteLock;
    }

    private void addCurrentThreadId(RedissonLockType lockType) {
        long threadId = Thread.currentThread().getId();

        List<Long> lockThreadIds = lockThreadIdMap.get(lockType);
        if (!lockThreadIds.contains(threadId)) {
            lockThreadIds.add(threadId);
        }
    }

    private void removeCurrentThreadId(RedissonLockType lockType) {
        long threadId = Thread.currentThread().getId();

        List<Long> lockThreadIds = lockThreadIdMap.get(lockType);
        if (lockThreadIds.contains(threadId)) {
            lockThreadIds.remove(threadId);
        }
    }

    // 可能存在冗余的线程Id，来自于锁过期，但该方法主要提供给getHeldLocks和destroy
    // 1. getHeldLocks方法里有lock.isHeldByThread(heldThreadId)，冗余的线程Id返回false，不影响最终结果
    // 2. destroy方法里有lock.unlockAsync(heldThreadId)，冗余的线程Id去解锁无效，不影响最终效果
    private List<Long> getHeldThreadIds(RedissonLockType lockType) {
        return lockThreadIdMap.get(lockType);
    }

    // 锁被分布式持有，即被任意一个进程持有，在其它进程也视为被持有
    // 锁被本地持有，即根据线程ID列表，判断锁被某个线程持有
    public List<String> getHeldLocks(RedissonLockType lockType, RedissonLockHeldType lockHeldType) {
        Map<String, RLock> lockMap = getLockMap(lockType);

        List<String> heldLocks = new ArrayList<String>();
        for (Map.Entry<String, RLock> entry : lockMap.entrySet()) {
            String key = entry.getKey();
            RLock lock = entry.getValue();
            switch (lockHeldType) {
                case DISTRIBUTION:
                    if (lock.isLocked()) { // 分布式锁住
                        heldLocks.add(key);
                    }
                    break;
                case LOCAL:
                    List<Long> heldThreadIds = getHeldThreadIds(lockType);
                    if (CollectionUtils.isNotEmpty(heldThreadIds)) {
                        for (long heldThreadId : heldThreadIds) {
                            if (lock.isHeldByThread(heldThreadId)) {
                                heldLocks.add(key);
                                break;
                            }
                        }
                    }
                    break;
            }
        }

        return heldLocks;
    }

    private Map<String, RLock> getLockMap(RedissonLockType lockType) {
        switch (lockType) {
            case UNFAIR:
                return unfairLockMap;
            case FAIR:
                return fairLockMap;
            case READ:
                return readLockMap;
            case WRITE:
                return writeLockMap;
        }

        return null;
    }

    public void destroy() {
        if (!isStarted()) {
            return;
        }

        try {
            destroy(RedissonLockType.UNFAIR);
            destroy(RedissonLockType.FAIR);
            destroy(RedissonLockType.READ);
            destroy(RedissonLockType.WRITE);
        } catch (Exception e) {
            LOG.info("Failed to destroy Redisson Held Locks", e);
        }

        LOG.info("Start to shutdown RedissonLock...");
        redissonClient.shutdown();
    }

    private void destroy(RedissonLockType lockType) {
        List<Long> heldThreadIds = getHeldThreadIds(lockType);
        if (CollectionUtils.isNotEmpty(heldThreadIds)) {
            List<String> heldLocks = getHeldLocks(lockType, RedissonLockHeldType.LOCAL);
            Map<String, RLock> lockMap = getLockMap(lockType);
            for (Map.Entry<String, RLock> entry : lockMap.entrySet()) {
                RLock lock = entry.getValue();
                for (long heldThreadId : heldThreadIds) {
                    // 为减轻网络开销，优化判断，减少跟Redis Server的交互
                    // 读锁可以同时被多个线程持有，其它锁只能同时被一个线程持有
                    if (lockType == RedissonLockType.READ) {
                        lock.unlockAsync(heldThreadId);
                    } else {
                        if (lock.isHeldByThread(heldThreadId)) {
                            lock.unlockAsync(heldThreadId);
                            break;
                        }
                    }
                }
            }

            LOG.info("Redisson Held Locks with {}Locks={} were destroyed", lockType, heldLocks);
        }
    }

    public boolean isStarted() {
        return redissonClient != null && !redissonClient.isShutdown() && !redissonClient.isShuttingDown();
    }
}