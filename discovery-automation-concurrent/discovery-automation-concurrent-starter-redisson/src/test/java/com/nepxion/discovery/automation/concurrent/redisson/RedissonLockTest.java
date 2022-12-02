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

import com.nepxion.discovery.automation.concurrent.redisson.lock.RedissonLock;
import com.nepxion.discovery.automation.concurrent.redisson.lock.RedissonLockHeldType;
import com.nepxion.discovery.automation.concurrent.redisson.lock.RedissonLockType;
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
        // testLock3(redissonLock);
        // testLock4(redissonLock);
        // testLock5(redissonLock);
        // testLock6(redissonLock);
        // testLock7(redissonLock);
    }

    public static void testLock0(RedissonLock redissonLock) {
        LOG.info("当前被持有的锁列表和线程Id列表 : nonfairLocks={}, fairLocks={}, readLocks={}, writeLocks={}, spinLocks={}",
                redissonLock.getHeldLocks(RedissonLockType.NONFAIR, RedissonLockHeldType.LOCAL),
                redissonLock.getHeldLocks(RedissonLockType.FAIR, RedissonLockHeldType.LOCAL),
                redissonLock.getHeldLocks(RedissonLockType.READ, RedissonLockHeldType.LOCAL),
                redissonLock.getHeldLocks(RedissonLockType.WRITE, RedissonLockHeldType.LOCAL),
                redissonLock.getHeldLocks(RedissonLockType.SPIN, RedissonLockHeldType.LOCAL));
    }

    public static void testLock1(RedissonLock redissonLock) {
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testLock0(redissonLock);

                            boolean acquired = redissonLock.tryLock(RedissonLockType.NONFAIR, "Discovery", 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");

                            testLock0(redissonLock);

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    redissonLock.unlock(RedissonLockType.NONFAIR, "Discovery");
                                    LOG.info("{}", "=== 释放锁 ===");
                                }
                            }

                            index++;
                        }
                    }).start();
                }
            }
        }, 0L, 500L);
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
                            testLock0(redissonLock);

                            boolean acquired = redissonLock.tryLock(RedissonLockType.WRITE, "Discovery", 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "1.【写】☎☎☎ 拿到锁 ☎☎☎..." : "1.【写】未拿到锁...");

                            testLock0(redissonLock);

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    redissonLock.unlock(RedissonLockType.WRITE, "Discovery");
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
                            testLock0(redissonLock);

                            boolean acquired = redissonLock.tryLock(RedissonLockType.READ, "Discovery", 0, 10, TimeUnit.SECONDS);
                            LOG.info("{}", acquired ? "2.【读】☎☎☎ 拿到锁 ☎☎☎..." : "2.【读】未拿到锁...");

                            testLock0(redissonLock);

                            if (acquired) {
                                redissonLock.unlock(RedissonLockType.READ, "Discovery");
                                LOG.info("{}", "=== 2.【读】释放锁 ===");
                            }
                        }
                    }).start();
                }
            }
        }, 0L, 3000L);
    }

    // 公平锁是指多个线程按照申请锁的顺序来获取锁，类似排队，先来后到
    // 公平锁在并发环境中，每个线程在获取锁时会先查看此锁维护的等待队列，如果为空，或者当前线程是等待队列的第一个，就占有锁，否则就会加入到等待队列中，以后会按照FIFO的规则从队列中取到自己
    // 非公平锁是指多个线程获取锁的顺序并不是按照申请锁的顺序，有可能后申请的线程比先申请的线程优先获取锁
    // 非公平锁比较粗鲁，上来就直接尝试占有锁，如果尝试失败，就再采用类似公平锁那种方式。 非公平锁的优点在于吞吐量比公平锁大，但在高并发的情况下，有可能会造成优先级反转或者饥饿现象（即有些线程在非公平情况下，始终无法被调度到） 
    public static void testLock3(RedissonLock redissonLock) {
        // 线程运行数小于CPU核数，测试才准确
        for (; index < 8; index++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                private int i = index;

                @Override
                public void run() {
                    // 公平锁下，8个线程一起去拿锁，只有第1个拿到，另外7个等待10秒后全部拿不到锁（Redisson Bug？）
                    // https://github.com/redisson/redisson/issues/4705
                    boolean acquired = redissonLock.tryLock(RedissonLockType.FAIR, "Discovery", 10, 1, TimeUnit.SECONDS);
                    LOG.info(i + ".公平锁 - {}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");
                }
            }).start();
        }
    }

    public static void testLock4(RedissonLock redissonLock) {
        // 线程运行数小于CPU核数，测试才准确
        for (; index < 8; index++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                private int i = index;

                @Override
                public void run() {
                    // 非公平锁下，8个线程一起去拿锁，“随机”一个拿到，1秒过期后，另外7个去抢占，最后依次拿到锁
                    boolean acquired = redissonLock.tryLock(RedissonLockType.NONFAIR, "Discovery", 10, 1, TimeUnit.SECONDS);
                    LOG.info(i + ".非公平锁 - {}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");
                }
            }).start();
        }
    }

    public static void testLock5(RedissonLock redissonLock) {
        for (; index < 8; index++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                private int i = index;

                @Override
                public void run() {
                    boolean acquired = redissonLock.tryLock(RedissonLockType.FAIR, "Discovery" + i, 1, 2, TimeUnit.HOURS);
                    LOG.info(i + ".公平锁 - {}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");
                }
            }).start();
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        redissonLock.destroy();

        System.exit(0);
    }

    public static void testLock6(RedissonLock redissonLock) {
        for (; index < 8; index++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                private int i = index;

                @Override
                public void run() {
                    boolean acquired = redissonLock.tryLock(RedissonLockType.NONFAIR, "Discovery" + i, 1, 2, TimeUnit.HOURS);
                    LOG.info(i + ".非公平锁 - {}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");
                }
            }).start();
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        redissonLock.destroy();

        System.exit(0);
    }

    // 当一个线程尝试去获取某一把锁的时候，如果这个锁已经被另外一个线程占有了，那么此线程就无法获取这把锁，该线程会等待，间隔一段时间后再次尝试获取
    // 这种采用循环加锁，等待锁释放的机制就称为自旋锁
    public static void testLock7(RedissonLock redissonLock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testLock0(redissonLock);

                // 对于一般锁来说，lock不会阻塞线程，而自旋锁需要阻塞才拿到锁
                // 该测试用例，运行第一遍，可以立刻拿到锁；运行第二遍，需要阻塞，等待第一遍的锁过期
                redissonLock.lock(RedissonLockType.SPIN, "Discovery", 30, TimeUnit.SECONDS);
                LOG.info("1.自旋锁 - {}", "☎☎☎ 拿到锁 ☎☎☎...");

                testLock0(redissonLock);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                testLock0(redissonLock);

                // tryLock不会阻塞，实时返回（Redisson Bug？）
                boolean acquired = redissonLock.tryLock(RedissonLockType.SPIN, "Discovery", 0, 30, TimeUnit.SECONDS);
                LOG.info("2.自旋锁 - {}", acquired ? "☎☎☎ 拿到锁 ☎☎☎..." : "未拿到锁...");

                testLock0(redissonLock);
            }
        }).start();
    }
}