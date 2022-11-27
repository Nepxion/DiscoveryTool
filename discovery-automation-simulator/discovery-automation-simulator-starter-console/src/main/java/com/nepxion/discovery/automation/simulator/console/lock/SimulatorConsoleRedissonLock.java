package com.nepxion.discovery.automation.simulator.console.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleRedissonLockProperties;
import com.nepxion.discovery.automation.common.console.lock.ConsoleRedissonLock;
import com.nepxion.discovery.automation.common.util.TestUtil;

public class SimulatorConsoleRedissonLock extends SimulatorConsoleLock {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorConsoleRedissonLock.class);

    @Autowired
    private ConsoleRedissonLockProperties consoleRedissonLockProperties;

    private ConsoleRedissonLock consoleRedissonLock;

    @Override
    public void initializeLock() {
        int waitSeconds = consoleRedissonLockProperties.getWaitSeconds();
        int expireSeconds = consoleRedissonLockProperties.getExpireSeconds();

        String content = TestUtil.getContent(ConsoleConstant.CONSOLE_AUTOMATION_FILE_PATH_REDISSON);

        Config config = null;
        try {
            config = Config.fromYAML(content);
        } catch (IOException e) {
            LOG.error("Initialize RedissonClient failed", e);

            return;
        }

        consoleRedissonLock = new ConsoleRedissonLock(config, waitSeconds, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getLockType() {
        return ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE_REDISSON;
    }

    @Override
    public boolean tryLock(String key) {
        return consoleRedissonLock.tryLock(key);
    }

    @Override
    public void unlock(String key) {
        consoleRedissonLock.unlock(key);
    }
}