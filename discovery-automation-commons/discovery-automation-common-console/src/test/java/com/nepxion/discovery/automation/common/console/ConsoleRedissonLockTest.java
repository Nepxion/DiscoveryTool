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

        ConsoleRedissonLock consoleLock = new ConsoleRedissonLock(config, 0, 10, TimeUnit.SECONDS);

        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean acquired = consoleLock.tryLock("Lock");
                            LOG.info("{}", acquired ? "拿到锁..." : "未拿到锁...");

                            if (index % 2 == 0) {
                                try {
                                    Thread.sleep(7000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (acquired) {
                                    consoleLock.unlock("Lock");

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