package com.nepxion.discovery.automation.concurrent.redisson.processor;

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
import com.nepxion.discovery.common.lock.DiscoveryLockHeldType;

public class RedissonLockProcessor implements DiscoveryLock, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonLockProcessor.class);

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
        // 默认只实现普通锁，不考虑读写分离锁
        return tryLock(RedissonLockType.LOCK, key, false);
    }

    public boolean tryLock(RedissonLockType lockType, String key, boolean fair) {
        return redissonLock.tryLock(lockType, key, fair, waitSeconds, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String key) {
        // 默认只实现普通锁，不考虑读写分离锁
        unlock(RedissonLockType.LOCK, key, false);
    }

    public void unlock(RedissonLockType lockType, String key, boolean fair) {
        redissonLock.unlock(lockType, key, fair);
    }

    @Override
    public List<String> getHeldLocks(DiscoveryLockHeldType lockHeldType) {
        // 默认只实现普通锁，不考虑读写分离锁
        return getHeldLocks(RedissonLockType.LOCK, lockHeldType, true);
    }

    public List<String> getHeldLocks(RedissonLockType lockType, DiscoveryLockHeldType lockHeldType, boolean ignoreSuffix) {
        switch (lockType) {
            case LOCK:
                return redissonLock.getHeldLocks(lockHeldType, ignoreSuffix);
            case READ_LOCK:
                return redissonLock.getHeldReadLocks(lockHeldType);
            case WRITE_LOCK:
                return redissonLock.getHeldWriteLocks(lockHeldType);
        }

        return null;
    }

    @Override
    public void destroy() throws Exception {
        redissonLock.destroy();
    }
}