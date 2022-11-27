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
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class ConsoleRedissonLock {
    private RedissonClient redissonClient;

    private long waitTime;
    private long leaseTime;
    private TimeUnit timeUnit;

    // 不需要考虑锁删除
    private volatile Map<String, RLock> lockMap;

    public ConsoleRedissonLock(Config config, long waitTime, long leaseTime, TimeUnit timeUnit) {
        this(config);

        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.timeUnit = timeUnit;
    }

    public ConsoleRedissonLock(Config config) {
        redissonClient = Redisson.create(config);

        lockMap = new ConcurrentHashMap<String, RLock>();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public boolean tryLock(String key) {
        return tryLock(key, waitTime, leaseTime, timeUnit);
    }

    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock lock = getLock(key);

        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(String key) {
        RLock lock = getLock(key);

        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    private RLock getLock(String key) {
        RLock lock = lockMap.get(key);
        if (lock == null) {
            RLock newLock = redissonClient.getLock(key);
            lock = lockMap.putIfAbsent(key, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }
}