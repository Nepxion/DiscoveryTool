package com.nepxion.discovery.automation.common.console;

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

import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.lock.ConsoleRedissonLock;
import com.nepxion.discovery.automation.common.console.lock.ConsoleRedissonLock.LockType;
import com.nepxion.discovery.automation.common.util.TestUtil;

public class ConsoleRedissonLockTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleRedissonLockTest.class);

    private static int index = 0;

    public static void main(String[] args) {
        String content = TestUtil.getContent(ConsoleConstant.CONSOLE_AUTOMATION_FILE_PATH_REDISSON);

        Config config = null;
        try {
            config = Config.fromYAML(content);
        } catch (IOException e) {
            LOG.error("Initialize RedissonClient failed", e);
        }

        ConsoleRedissonLock consoleLock = new ConsoleRedissonLock(config);

        // testLock1(consoleLock);
        testLock2(consoleLock);
    }

    public static void testLock1(ConsoleRedissonLock consoleLock) {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean acquired = consoleLock.tryLock(LockType.LOCK, "Lock", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "拿到锁..." : "未拿到锁...");

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    consoleLock.unlock(LockType.LOCK, "Lock", false);
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
    public static void testLock2(ConsoleRedissonLock consoleLock) {
        Timer time1 = new Timer();
        time1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean acquired = consoleLock.tryLock(LockType.WRITE_LOCK, "Lock", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "1.【写】拿到锁..." : "1.【写】未拿到锁...");

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    consoleLock.unlock(LockType.WRITE_LOCK, "Lock", false);
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
                            boolean acquired = consoleLock.tryLock(LockType.READ_LOCK, "Lock", false, 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "2.【读】拿到锁..." : "2.【读】未拿到锁...");

                            if (acquired) {
                                consoleLock.unlock(LockType.READ_LOCK, "Lock", false);
                                LOG.info("{}", "=== 2.【读】释放锁 ===");
                            }
                        }
                    }).start();
                }
            }
        }, 0L, 3000L);
    }
}