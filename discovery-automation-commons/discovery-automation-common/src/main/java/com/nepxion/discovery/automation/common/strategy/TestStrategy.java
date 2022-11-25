package com.nepxion.discovery.automation.common.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.automation.common.runner.TestCaseContext;

public class TestStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TestStrategy.class);

    private TestCaseContext testCaseContext;

    private long testCaseStartTime;

    public TestStrategy() {
        beforeTest();
    }

    public TestCaseContext getTestCaseContext() {
        return testCaseContext;
    }

    public void setTestCaseContext(TestCaseContext testCaseContext) {
        this.testCaseContext = testCaseContext;
    }

    public void beforeTest() {
        testCaseStartTime = System.currentTimeMillis();
    }

    public void afterTest() {
        LOG.info("* Finished all automation testcases in {} seconds", (System.currentTimeMillis() - testCaseStartTime) / 1000);
    }
}