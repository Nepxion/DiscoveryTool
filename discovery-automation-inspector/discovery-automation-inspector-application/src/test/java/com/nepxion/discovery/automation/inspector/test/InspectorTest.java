package com.nepxion.discovery.automation.inspector.test;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nepxion.discovery.automation.common.application.TestApplication;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseProperty;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InspectorTest {
    @Autowired
    private InspectorTestCaseProperty testCaseProperty;

    @Autowired
    private InspectorTestCaseCondition testCaseCondition;

    @Autowired
    private InspectorTestRunner testRunner;

    private static InspectorTestStrategy testStrategy;

    @BeforeClass
    public static void beforeTest() {
        InspectorTestRunner.beforeTest();
    }

    @AfterClass
    public static void afterTest() {
        InspectorTestRunner.afterTest();
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