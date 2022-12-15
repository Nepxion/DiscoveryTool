package com.nepxion.discovery.automation.concurrent.caffeine.lock;

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
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CaffeineLock {
    private static final Logger LOG = LoggerFactory.getLogger(CaffeineLock.class);

    private LoadingCache<String, String> loadingCache;
    private Lock lock = new ReentrantLock();

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

    public boolean tryLock(String key) {
        lock.lock();
        try {
            boolean absent = loadingCache.getIfPresent(key) == null;
            if (absent) {
                loadingCache.put(key, Boolean.TRUE.toString());
            }

            return absent;
        } finally {
            lock.unlock();
        }
    }

    public void lock(String key) {
        lock.lock();
        try {
            loadingCache.invalidate(key);
            loadingCache.put(key, Boolean.TRUE.toString());
        } finally {
            lock.unlock();
        }
    }

    public void unlock(String key) {
        lock.lock();
        try {
            boolean absent = loadingCache.getIfPresent(key) == null;
            if (!absent) {
                loadingCache.invalidate(key);
            }
        } finally {
            lock.unlock();
        }
    }

    public List<String> getHeldLocks() {
        Set<String> set = loadingCache.asMap().keySet();

        return new ArrayList<String>(set);
    }

    public void destroy() {
        List<String> heldLocks = getHeldLocks();

        LOG.info("Start to destroy Caffeine Held Locks with locks={}", heldLocks);
        // loadingCache.invalidateAll();
    }

    public void onKeyRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {

    }
}