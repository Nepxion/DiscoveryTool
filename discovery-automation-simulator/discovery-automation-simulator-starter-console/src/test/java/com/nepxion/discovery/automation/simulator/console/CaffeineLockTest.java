package com.nepxion.discovery.automation.simulator.console;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CaffeineLockTest {
    private static int index = 0;

    public static void main(String[] args) {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(100)
                .recordStats()
                .evictionListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                        if (cause == RemovalCause.EXPIRED) {
                            System.out.println("=== 锁过期 ===");
                        }
                    }
                })
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return StringUtils.EMPTY;
                    }
                });

        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                loadingCache.put("a", "1");
                System.out.println("=== 加上锁 ===");

                if (index % 2 == 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    loadingCache.invalidate("a");
                    System.out.println("=== 释放锁 ===");
                }

                index++;
            }
        }, 0L, 10000L);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(loadingCache.getIfPresent("a") != null ? "锁存在..." : "锁不存在...");
                        }
                    }).start();
                }
            }
        }, 1000L, 100L);
    }
}