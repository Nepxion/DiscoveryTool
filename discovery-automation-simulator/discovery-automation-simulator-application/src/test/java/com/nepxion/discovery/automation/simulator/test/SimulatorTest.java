package com.nepxion.discovery.automation.simulator.test;

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
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConditionRoute;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseProperty;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimulatorTest {
    @Autowired
    private SimulatorTestCaseProperty testCaseProperty;

    @Autowired
    private SimulatorTestCaseCondition basicTestCaseCondition;

    @Autowired
    private SimulatorTestCaseCondition releaseTestCaseCondition;

    @Autowired
    private SimulatorTestCaseConditionRoute releaseTestCaseConditionRoute;

    @Autowired
    private SimulatorTestRunner testRunner;

    private static SimulatorTestStrategy testStrategy;

    @BeforeClass
    public static void beforeTest() {
        SimulatorTestRunner.beforeTest();
    }

    @AfterClass
    public static void afterTest() {
        SimulatorTestRunner.afterTest();
    }

    @Test
    public void test0Initialization() throws Exception {
        testStrategy = testRunner.testInitialization(testCaseProperty, basicTestCaseCondition, releaseTestCaseCondition, releaseTestCaseConditionRoute);
    }

    @Test
    public void test1Normal() throws Exception {
        testRunner.testNormal(testStrategy);
    }

    @Test
    public void test2FirstVersionBasicRelease() throws Exception {
        testRunner.testFirstVersionBasicRelease(testStrategy);
    }

    @Test
    public void test3FirstVersionBlueGreenGrayRelease() throws Exception {
        testRunner.testFirstVersionBlueGreenGrayRelease(testStrategy);
    }

    @Test
    public void test4FirstResetRelease() throws Exception {
        testRunner.testFirstResetRelease(testStrategy);
    }

    @Test
    public void test5SecondVersionBasicRelease() throws Exception {
        testRunner.testSecondVersionBasicRelease(testStrategy);
    }

    @Test
    public void test6SecondVersionBlueGreenGrayRelease() throws Exception {
        testRunner.testSecondVersionBlueGreenGrayRelease(testStrategy);
    }

    @Test
    public void test7SecondResetRelease() throws Exception {
        testRunner.testSecondResetRelease(testStrategy);
    }
}