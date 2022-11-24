package com.nepxion.discovery.automation.simulator.runner;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public abstract class SimulatorTestCaseRunner {
    private SimulatorTestCaseContext testCaseContext;
    private boolean enabled;

    public SimulatorTestCaseRunner(SimulatorTestCaseContext testCaseContext) {
        this(testCaseContext, true);
    }

    public SimulatorTestCaseRunner(SimulatorTestCaseContext testCaseContext, boolean enabled) {
        this.testCaseContext = testCaseContext;
        this.enabled = enabled;
    }

    public void start() throws Exception {
        if (!testCaseContext.isPassed() || !enabled) {
            return;
        }

        try {
            run();
        } catch (Throwable e) {
            testCaseContext.setPassed(false);

            throw e;
        }
    }

    public abstract void run() throws Exception;
}