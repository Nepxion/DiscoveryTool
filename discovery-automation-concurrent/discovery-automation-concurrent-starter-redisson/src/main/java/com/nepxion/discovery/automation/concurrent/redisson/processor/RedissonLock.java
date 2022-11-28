package com.nepxion.discovery.automation.concurrent.redisson.processor;

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
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissonLock {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonLock.class);

    private RedissonClient redissonClient;

    // 可重入锁可重复使用, 不需要考虑锁删除
    private volatile Map<String, RLock> lockMap;
    private volatile Map<String, RReadWriteLock> readWriteLockMap;

    public RedissonLock(Config config) {
        this(Redisson.create(config));
    }

    public RedissonLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;

        lockMap = new ConcurrentHashMap<String, RLock>();
        readWriteLockMap = new ConcurrentHashMap<String, RReadWriteLock>();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public boolean tryLock(RedissonLockType lockType, String key, boolean fair, long waitTime, long leaseTime, TimeUnit timeUnit) {
        if (!isStarted()) {
            return false;
        }

        RLock lock = getLock(lockType, key, fair);

        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(RedissonLockType lockType, String key, boolean fair) {
        if (!isStarted()) {
            return;
        }

        RLock lock = getLock(lockType, key, fair);

        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public RLock getLock(RedissonLockType lockType, String key, boolean fair) {
        switch (lockType) {
            case LOCK:
                return getCachedLock(key, fair);
            case READ_LOCK:
                return getCachedReadWriteLock(key).readLock();
            case WRITE_LOCK:
                return getCachedReadWriteLock(key).writeLock();
        }

        return null;
    }

    private RLock getCachedLock(String key, boolean fair) {
        String newKey = key + "-" + (fair ? "fair" : "unfair");

        RLock lock = lockMap.get(newKey);
        if (lock == null) {
            RLock newLock = getNewLock(newKey, fair);
            lock = lockMap.putIfAbsent(newKey, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RReadWriteLock getCachedReadWriteLock(String key) {
        String newKey = key;

        RReadWriteLock readWriteLock = readWriteLockMap.get(newKey);
        if (readWriteLock == null) {
            RReadWriteLock newReadWriteLock = getNewReadWriteLock(newKey);
            readWriteLock = readWriteLockMap.putIfAbsent(newKey, newReadWriteLock);
            if (readWriteLock == null) {
                readWriteLock = newReadWriteLock;
            }
        }

        return readWriteLock;
    }

    private RLock getNewLock(String key, boolean fair) {
        RLock lock = fair ? redissonClient.getFairLock(key) : redissonClient.getLock(key);

        return lock;
    }

    private RReadWriteLock getNewReadWriteLock(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);

        return readWriteLock;
    }

    public List<String> getHeldLocks() {
        List<String> heldLocks = new ArrayList<String>();
        for (Map.Entry<String, RLock> entry : lockMap.entrySet()) {
            String key = entry.getKey();
            RLock lock = entry.getValue();
            if (lock.isLocked()) {
                heldLocks.add(key.substring(0, key.lastIndexOf("-")));
            }
        }

        return heldLocks;
    }

    public List<String> getHeldReadLocks() {
        List<String> heldLocks = new ArrayList<String>();
        for (Map.Entry<String, RReadWriteLock> entry : readWriteLockMap.entrySet()) {
            String key = entry.getKey();
            RReadWriteLock lock = entry.getValue();
            if (lock.readLock().isLocked()) {
                heldLocks.add(key);
            }
        }

        return heldLocks;
    }

    public List<String> getHeldWriteLocks() {
        List<String> heldLocks = new ArrayList<String>();
        for (Map.Entry<String, RReadWriteLock> entry : readWriteLockMap.entrySet()) {
            String key = entry.getKey();
            RReadWriteLock lock = entry.getValue();
            if (lock.writeLock().isLocked()) {
                heldLocks.add(key);
            }
        }

        return heldLocks;
    }

    public void shutdown() {
        if (!isStarted()) {
            return;
        }

        LOG.info("Start to shutdown RedissonLock......");

        redissonClient.shutdown();
    }

    public boolean isStarted() {
        return redissonClient != null && !redissonClient.isShutdown() && !redissonClient.isShuttingDown();
    }
}