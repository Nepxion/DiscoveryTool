package com.nepxion.discovery.automation.concurrent.redisson.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.concurrent.redisson.entity.RedissonProperties;
import com.nepxion.discovery.common.lock.DiscoveryLock;

public class RedissonLockWrapper implements DiscoveryLock, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonLockWrapper.class);

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private RedissonClient redissonClient;

    private RedissonLock redissonLock;

    private int waitSeconds;
    private int expireSeconds;

    @PostConstruct
    private void initialize() {
        LOG.info("Start to initialize RedissonLock...");

        this.waitSeconds = redissonProperties.getWaitSeconds();
        this.expireSeconds = redissonProperties.getExpireSeconds();

        redissonLock = new RedissonLock(redissonClient);

        LOG.info("RedissonLock wait seconds : {}", waitSeconds);
        LOG.info("RedissonLock expire seconds : {}", expireSeconds);
    }

    @Override
    public boolean tryLock(String key) {
        // 默认只实现非公平锁
        return tryLock(RedissonLockType.NONFAIR, key);
    }

    public boolean tryLock(RedissonLockType lockType, String key) {
        return redissonLock.tryLock(lockType, key, waitSeconds, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String key) {
        // 默认只实现非公平锁
        lock(RedissonLockType.NONFAIR, key);
    }

    public void lock(RedissonLockType lockType, String key) {
        redissonLock.lock(lockType, key, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String key) {
        // 默认只实现非公平锁
        unlock(RedissonLockType.NONFAIR, key);
    }

    public void unlock(RedissonLockType lockType, String key) {
        redissonLock.unlock(lockType, key);
    }

    @Override
    public List<String> getHeldLocks() {
        // 默认只实现非公平锁
        return getHeldLocks(RedissonLockType.NONFAIR, RedissonLockHeldType.DISTRIBUTION);
    }

    public List<String> getHeldLocks(RedissonLockType lockType, RedissonLockHeldType lockHeldType) {
        return redissonLock.getHeldLocks(lockType, lockHeldType);
    }

    @Override
    public void destroy() throws Exception {
        redissonLock.destroy();
    }
}