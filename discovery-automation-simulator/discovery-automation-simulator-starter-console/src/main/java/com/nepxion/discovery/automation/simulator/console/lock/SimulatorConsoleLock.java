package com.nepxion.discovery.automation.simulator.console.lock;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 通过线程安全的锁组件（本地锁或者分布式锁）并行控制测试用例，根据Key（group@serviceId）进行判断，不允许有多个Key相同的测试用例同时运行
public abstract class SimulatorConsoleLock {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorConsoleLock.class);

    @PostConstruct
    private void initialize() {
        LOG.info("Lock for {} starts to initialize...", getLockType());

        initializeLock();
    }

    // 初始化并行控制锁
    public abstract void initializeLock();

    // 返回并行控制锁的名称
    public abstract String getLockType();

    // 新发起的测试用例尝试获取锁
    public abstract boolean tryLock(String key);

    // 测试用例结束或者中途抛错释放锁
    public abstract void unlock(String key);
}