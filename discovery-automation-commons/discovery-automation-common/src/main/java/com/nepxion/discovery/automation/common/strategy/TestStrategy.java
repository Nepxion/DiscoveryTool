package com.nepxion.discovery.automation.common.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.runner.TestCaseContext;

public class TestStrategy {
    private TestCaseContext testCaseContext;

    public TestCaseContext getTestCaseContext() {
        return testCaseContext;
    }

    public void setTestCaseContext(TestCaseContext testCaseContext) {
        this.testCaseContext = testCaseContext;
    }
}