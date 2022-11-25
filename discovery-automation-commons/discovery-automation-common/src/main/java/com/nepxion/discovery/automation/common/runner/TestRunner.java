package com.nepxion.discovery.automation.common.runner;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.strategy.TestStrategy;

public class TestRunner {
    public void beforeTest(TestStrategy testStrategy) {
        testStrategy.beforeTest();
    }

    public void afterTest(TestStrategy testStrategy) {
        testStrategy.afterTest();
    }
}