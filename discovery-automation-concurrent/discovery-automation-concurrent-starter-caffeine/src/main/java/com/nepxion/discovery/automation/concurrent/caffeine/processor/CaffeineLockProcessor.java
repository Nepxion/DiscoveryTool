package com.nepxion.discovery.automation.concurrent.caffeine.processor;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import javax.annotation.PostConstruct;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.nepxion.discovery.automation.concurrent.caffeine.entity.CaffeineProperties;
import com.nepxion.discovery.common.lock.DiscoveryLock;

public class CaffeineLockProcessor implements DiscoveryLock, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(CaffeineLockProcessor.class);

    @Autowired
    private CaffeineProperties caffeineProperties;

    private CaffeineLock caffeineLock;

    @PostConstruct
    private void initialize() {
        LOG.info("Start to initialize CaffeineLock...");

        int initialCapacity = caffeineProperties.getInitialCapacity();
        int maximumSize = caffeineProperties.getMaximumSize();
        int expireSeconds = caffeineProperties.getExpireSeconds();

        caffeineLock = new CaffeineLock(initialCapacity, maximumSize, expireSeconds) {
            @Override
            public void onKeyRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                if (cause == RemovalCause.EXPIRED) {
                    LOG.info("Key={} is expired...", key);
                }
            }
        };

        LOG.info("CaffeineLock initial capacity : {}", initialCapacity);
        LOG.info("CaffeineLock maximum size : {}", maximumSize);
        LOG.info("CaffeineLock expire seconds : {}", expireSeconds);
    }

    @Override
    public boolean tryLock(String key) {
        return caffeineLock.tryLock(key);
    }

    @Override
    public void unlock(String key) {
        caffeineLock.unlock(key);
    }

    @Override
    public List<String> getHeldLocks() {
        // Caffeine只支持锁被本地持有的场景
        return caffeineLock.getHeldLocks();
    }

    @Override
    public void destroy() throws Exception {
        caffeineLock.destroy();
    }
}