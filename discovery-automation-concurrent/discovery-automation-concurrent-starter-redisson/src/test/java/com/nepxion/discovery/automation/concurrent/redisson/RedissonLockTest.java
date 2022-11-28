package com.nepxion.discovery.automation.concurrent.redisson;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.automation.concurrent.redisson.processor.RedissonLock;
import com.nepxion.discovery.automation.concurrent.redisson.processor.RedissonLockType;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.property.DiscoveryContent;

public class RedissonLockTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonLockTest.class);

    private static int index = 0;

    public static void main(String[] args) {
        Config config = null;
        try {
            DiscoveryContent discoveryContent = new DiscoveryContent("redisson.yaml", DiscoveryConstant.ENCODING_UTF_8);
            String content = discoveryContent.getContent();

            config = Config.fromYAML(content);
        } catch (IOException e) {
            LOG.error("Initialize RedissonClient failed", e);
        }

        RedissonLock redissonLock = new RedissonLock(config);

        testLock1(redissonLock);
        // testLock2(redissonLock);
    }

    public static void testLock1(RedissonLock redissonLock) {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            boolean acquired = redissonLock.tryLock(RedissonLockType.LOCK, "Lock", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "拿到锁..." : "未拿到锁...");

                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    redissonLock.unlock(RedissonLockType.LOCK, "Lock", false);
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

    // 写锁是独占锁，读锁是共享锁
    // 只有一个线程可以拿到写锁，可以有多个线程拿到读锁
    // 写锁和读锁互斥，当写锁被持有后，读锁就拿不到，当读锁被持有后，写锁也拿不到
    public static void testLock2(RedissonLock redissonLock) {
        Timer time1 = new Timer();
        time1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            boolean acquired = redissonLock.tryLock(RedissonLockType.WRITE_LOCK, "Lock-RW", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "1.【写】拿到锁..." : "1.【写】未拿到锁...");

                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    redissonLock.unlock(RedissonLockType.WRITE_LOCK, "Lock-RW", false);
                                    LOG.info("{}", "=== 1.【写】释放锁 ===");
                                }
                            }

                            index++;
                        }
                    }).start();
                }
            }
        }, 0L, 5000L);

        Timer time2 = new Timer();
        time2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            boolean acquired = redissonLock.tryLock(RedissonLockType.READ_LOCK, "Lock-RW", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "2.【读】拿到锁..." : "2.【读】未拿到锁...");

                            LOG.info("当前被持有的锁列表 : locks={}, writeLocks={}, readLocks={}", redissonLock.getHeldLocks(false), redissonLock.getHeldWriteLocks(), redissonLock.getHeldReadLocks());

                            if (acquired) {
                                redissonLock.unlock(RedissonLockType.READ_LOCK, "Lock-RW", false);
                                LOG.info("{}", "=== 2.【读】释放锁 ===");
                            }
                        }
                    }).start();
                }
            }
        }, 0L, 3000L);
    }
}