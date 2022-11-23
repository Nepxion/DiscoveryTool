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

import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseEntity;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

public class InspectorTestRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InspectorTestRunner.class);

    @Autowired
    private InspectorTestCases testCases;

    private static long startTime;

    public static void beforeTest() {
        startTime = System.currentTimeMillis();
    }

    public static void afterTest() {
        LOG.info("* Finished all automation testcases in {} seconds", (System.currentTimeMillis() - startTime) / 1000);
    }

    public InspectorTestStrategy testInitialization(String testCaseEntityContent, String testCaseConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        return testInitialization(0, testCaseEntityContent, testCaseConditionContent, testCaseConfigWithYaml);
    }

    public InspectorTestStrategy testInitialization(InspectorTestCaseEntity testCaseEntity, InspectorTestCaseCondition testCaseCondition) throws Exception {
        return testInitialization(0, testCaseEntity, testCaseCondition);
    }

    public void testInspection(InspectorTestStrategy testStrategy) throws Exception {
        testInspection(1, testStrategy);
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
}