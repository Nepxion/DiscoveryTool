package com.nepxion.discovery.automation.concurrent.caffeine.processor;

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

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CaffeineLock {
    private LoadingCache<String, String> loadingCache;

    public CaffeineLock(int initialCapacity, int maximumSize, int expireSeconds) {
        loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .recordStats()
                .evictionListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                        onKeyRemoval(key, value, cause);
                    }
                })
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return StringUtils.EMPTY;
                    }
                });
    }

    public LoadingCache<String, String> getLoadingCache() {
        return loadingCache;
    }

    public synchronized boolean tryLock(String key) {
        boolean acquired = loadingCache.getIfPresent(key) == null;
        if (acquired) {
            loadingCache.put(key, Boolean.TRUE.toString());
        }

        return acquired;
    }

    public synchronized void unlock(String key) {
        boolean acquired = loadingCache.getIfPresent(key) == null;
        if (!acquired) {
            loadingCache.invalidate(key);
        }
    }

    public void onKeyRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {

    }
}