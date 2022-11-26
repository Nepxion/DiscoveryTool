package com.nepxion.discovery.automation.simulator.console.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleCaffeineLockProperties;

public class SimulatorConsoleCaffeineLock extends SimulatorConsoleLock {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorConsoleCaffeineLock.class);

    @Autowired
    private ConsoleCaffeineLockProperties consoleCaffeineLockProperties;

    private LoadingCache<String, String> loadingCache;

    @Override
    public void initializeLock() {
        int initialCapacity = consoleCaffeineLockProperties.getInitialCapacity();
        int maximumSize = consoleCaffeineLockProperties.getMaximumSize();
        int expireSeconds = consoleCaffeineLockProperties.getExpireSeconds();

        loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .evictionListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                        if (cause == RemovalCause.EXPIRED) {
                            LOG.info("Key={} is expired...", key);
                        }
                    }
                })
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return StringUtils.EMPTY;
                    }
                });
    }

    @Override
    public String getLockType() {
        return ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE_CAFFEINE;
    }

    @Override
    public boolean isLocked(String key) {
        return loadingCache.getIfPresent(key) != null;
    }

    @Override
    public void lock(String key) {
        loadingCache.put(key, Boolean.TRUE.toString());
    }

    @Override
    public void unlock(String key) {
        loadingCache.invalidate(key);
    }
}