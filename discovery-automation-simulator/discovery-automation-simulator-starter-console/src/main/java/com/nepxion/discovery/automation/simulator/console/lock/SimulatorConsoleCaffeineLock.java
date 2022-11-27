package com.nepxion.discovery.automation.simulator.console.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleCaffeineLockProperties;
import com.nepxion.discovery.automation.common.console.lock.ConsoleCaffeineLock;

public class SimulatorConsoleCaffeineLock extends SimulatorConsoleLock {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorConsoleCaffeineLock.class);

    @Autowired
    private ConsoleCaffeineLockProperties consoleCaffeineLockProperties;

    private ConsoleCaffeineLock consoleCaffeineLock;

    @Override
    public void initializeLock() {
        int initialCapacity = consoleCaffeineLockProperties.getInitialCapacity();
        int maximumSize = consoleCaffeineLockProperties.getMaximumSize();
        int expireSeconds = consoleCaffeineLockProperties.getExpireSeconds();

        consoleCaffeineLock = new ConsoleCaffeineLock(initialCapacity, maximumSize, expireSeconds) {
            @Override
            public void onKeyRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                if (cause == RemovalCause.EXPIRED) {
                    LOG.info("Key={} is expired...", key);
                }
            }
        };
    }

    @Override
    public String getLockType() {
        return ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE_CAFFEINE;
    }

    @Override
    public boolean tryLock(String key) {
        return consoleCaffeineLock.tryLock(key);
    }

    @Override
    public void unlock(String key) {
        consoleCaffeineLock.unlock(key);
    }
}