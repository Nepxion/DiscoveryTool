package com.nepxion.discovery.automation.inspector.runner;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.common.runner.TestCaseContext;
import com.nepxion.discovery.automation.common.runner.TestCaseRunner;
import com.nepxion.discovery.automation.common.runner.TestRunner;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseEntity;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

public class InspectorTestRunner extends TestRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InspectorTestRunner.class);

    @Autowired
    private InspectorTestCases testCases;

    public InspectorTestStrategy testInitialization(String testCaseEntityContent, String testCaseConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        InspectorTestStrategy testStrategy = new TestCaseRunner<InspectorTestStrategy>(testCaseContext) {
            @Override
            public InspectorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntityContent, testCaseConditionContent, testCaseConfigWithYaml);
            }
        }.start();

        testStrategy.setTestCaseContext(testCaseContext);

        return testStrategy;
    }

    public InspectorTestStrategy testInitialization(InspectorTestCaseEntity testCaseEntity, InspectorTestCaseCondition testCaseCondition) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        InspectorTestStrategy testStrategy = new TestCaseRunner<InspectorTestStrategy>(testCaseContext) {
            @Override
            public InspectorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntity, testCaseCondition);
            }
        }.start();

        testStrategy.setTestCaseContext(testCaseContext);

        return testStrategy;
    }

    public void testInspection(InspectorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testInspection(1, testStrategy);

                return null;
            }
        }.start();
    }

    public InspectorTestStrategy testInitialization(int sceneIndex, String testCaseEntityContent, String testCaseConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【侦测场景{}】初始化上下文...", sceneIndex);

        long startTime = System.currentTimeMillis();
        InspectorTestStrategy testStrategy = new InspectorTestStrategy();
        testStrategy.testInitialization(testCaseEntityContent, testCaseConditionContent, testCaseConfigWithYaml);
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        return testStrategy;
    }

    public InspectorTestStrategy testInitialization(int sceneIndex, InspectorTestCaseEntity testCaseEntity, InspectorTestCaseCondition testCaseCondition) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【侦测场景{}】初始化上下文...", sceneIndex);

        long startTime = System.currentTimeMillis();
        InspectorTestStrategy testStrategy = new InspectorTestStrategy();
        testStrategy.testInitialization(testCaseEntity, testCaseCondition);
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        return testStrategy;
    }

    public void testInspection(int sceneIndex, InspectorTestStrategy testStrategy) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【侦测场景{}】测试全链路侦测...", sceneIndex);

        long startTime = System.currentTimeMillis();
        testCases.testInspection(testStrategy);
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}