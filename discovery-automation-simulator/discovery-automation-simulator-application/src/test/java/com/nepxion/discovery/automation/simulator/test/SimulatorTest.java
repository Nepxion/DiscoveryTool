package com.nepxion.discovery.automation.simulator.test;

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
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseProperty;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseBasicCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseFirstCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseSecondCondition;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class SimulatorTest {
    @Autowired
    private SimulatorTestCaseProperty testCaseProperty;

    @Autowired
    private SimulatorTestCaseReleaseBasicCondition testCaseReleaseBasicCondition;

    @Autowired
    private SimulatorTestCaseReleaseFirstCondition testCaseReleaseFirstCondition;

    @Autowired
    private SimulatorTestCaseReleaseSecondCondition testCaseReleaseSecondCondition;

    @Autowired
    private SimulatorTestRunner testRunner;

    private static SimulatorTestStrategy testStrategy;

    @BeforeAll
    public static void beforeTest() {

    }

    @AfterAll
    public static void afterTest() {
        testStrategy.afterTest();
    }

    @Test
    public void test0Initialization() throws Exception {
        testStrategy = testRunner.testInitialization(testCaseProperty, testCaseReleaseBasicCondition, testCaseReleaseFirstCondition, testCaseReleaseSecondCondition);
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