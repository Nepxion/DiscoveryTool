package com.nepxion.discovery.automation.concurrent.caffeine;

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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.nepxion.discovery.automation.concurrent.caffeine.processor.CaffeineLock;

public class CaffeineLockTest {
    private static final Logger LOG = LoggerFactory.getLogger(CaffeineLockTest.class);

    private static int index = 0;

    public static void main(String[] args) {
        CaffeineLock caffeineLock = new CaffeineLock(10, 100, 10) {
            @Override
            public void onKeyRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                if (cause == RemovalCause.EXPIRED) {
                    LOG.info("{}", "=== 锁过期===");
                }
            }
        };

        testLock1(caffeineLock);
    }

    public static void testLock1(CaffeineLock caffeineLock) {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean acquired = caffeineLock.tryLock("Lock");
                            LOG.info("{}", acquired ? "拿到锁..." : "未拿到锁...");

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    caffeineLock.unlock("Lock");
                                    LOG.info("{}", "=== 释放锁 ===");
                                }
                            }

                            index++;
                        }
                    }).start();
                }
            }
        }, 0L, 2000L);
    }
}