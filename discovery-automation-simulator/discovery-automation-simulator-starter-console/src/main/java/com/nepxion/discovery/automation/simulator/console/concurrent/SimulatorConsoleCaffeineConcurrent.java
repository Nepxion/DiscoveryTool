package com.nepxion.discovery.automation.simulator.console.concurrent;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleCaffeineCacheProperties;

public class SimulatorConsoleCaffeineConcurrent extends SimulatorConsoleConcurrent {
    @Autowired
    private ConsoleCaffeineCacheProperties consoleCaffeineCacheProperties;

    private LoadingCache<String, String> loadingCache;

    @Override
    public void initializeCache() {
        int initialCapacity = consoleCaffeineCacheProperties.getInitialCapacity();
        int maximumSize = consoleCaffeineCacheProperties.getMaximumSize();
        int expireSeconds = consoleCaffeineCacheProperties.getExpireSeconds();

        loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return StringUtils.EMPTY;
                    }
                });
    }

    @Override
    public String getCacheName() {
        return ConsoleConstant.CONSOLE_AUTOMATION_CACHE_TYPE_CAFFEINE;
    }

    @Override
    public boolean validateTest(String key) {
        return loadingCache.getIfPresent(key) != null;
    }

    @Override
    public void runTest(String key) {
        loadingCache.put(key, Boolean.TRUE.toString());
    }

    @Override
    public void finishTest(String key) {
        loadingCache.invalidate(key);
    }
}