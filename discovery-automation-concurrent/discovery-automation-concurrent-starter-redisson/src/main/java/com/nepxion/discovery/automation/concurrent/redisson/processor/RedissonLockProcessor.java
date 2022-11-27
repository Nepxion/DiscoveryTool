package com.nepxion.discovery.automation.concurrent.redisson.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.concurrent.redisson.entity.RedissonProperties;
import com.nepxion.discovery.common.lock.DiscoveryLock;

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
    }

    @Override
    public boolean tryLock(String key) {
        return redissonLock.tryLock(RedissonLockType.LOCK, key, false, waitSeconds, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String key) {
        redissonLock.unlock(RedissonLockType.LOCK, key, false);
    }

    @Override
    public void destroy() throws Exception {
        redissonLock.shutdown();
    }
}