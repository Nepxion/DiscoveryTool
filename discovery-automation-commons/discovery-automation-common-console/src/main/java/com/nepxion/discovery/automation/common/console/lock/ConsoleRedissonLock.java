package com.nepxion.discovery.automation.common.console.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleRedissonLock {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleRedissonLock.class);

    private RedissonClient redissonClient;

    // 可重入锁可重复使用, 不需要考虑锁删除
    private volatile Map<String, RLock> lockMap;
    private volatile Map<String, RReadWriteLock> readWriteLockMap;

    public ConsoleRedissonLock(Config config) {
        redissonClient = Redisson.create(config);

        lockMap = new ConcurrentHashMap<String, RLock>();
        readWriteLockMap = new ConcurrentHashMap<String, RReadWriteLock>();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public boolean tryLock(LockType lockType, String key, boolean fair, long waitTime, long leaseTime, TimeUnit timeUnit) {
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

    public void unlock(LockType lockType, String key, boolean fair) {
        if (!isStarted()) {
            return;
        }

        RLock lock = getLock(lockType, key, fair);

        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public RLock getLock(LockType lockType, String key, boolean fair) {
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

    public void shutdown() {
        if (!isStarted()) {
            return;
        }

        LOG.info("Lock for redisson starts to shutdown...");

        redissonClient.shutdown();
    }

    public boolean isStarted() {
        return redissonClient != null && !redissonClient.isShutdown() && !redissonClient.isShuttingDown();
    }

    public enum LockType {
        // 普通锁
        LOCK("Lock"),

        // 读锁
        READ_LOCK("ReadLock"),

        // 写锁
        WRITE_LOCK("WriteLock");

        private String value;

        private LockType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static LockType fromString(String value) {
            for (LockType type : LockType.values()) {
                if (type.getValue().equalsIgnoreCase(value.trim())) {
                    return type;
                }
            }

            throw new IllegalArgumentException("Mismatched type with value=" + value);
        }

        @Override
        public String toString() {
            return value;
        }
    }
}