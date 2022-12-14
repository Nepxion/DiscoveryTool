package com.nepxion.discovery.automation.inspector.test;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nepxion.discovery.automation.common.application.TestApplication;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseProperty;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class InspectorTest {
    @Autowired
    private InspectorTestCaseProperty testCaseProperty;

    @Autowired
    private InspectorTestCaseCondition testCaseCondition;

    @Autowired
    private InspectorTestRunner testRunner;

    private static InspectorTestStrategy testStrategy;

    @BeforeAll
    public static void beforeTest() {

    }

    @AfterAll
    public static void afterTest() {
        testStrategy.afterTest();
    }

    @Test
    public void test0Initialization() throws Exception {
        testStrategy = testRunner.testInitialization(testCaseProperty, testCaseCondition);
    }

    @Test
    public void test1Inspection() throws Exception {
        testRunner.testInspection(testStrategy);
    }
}