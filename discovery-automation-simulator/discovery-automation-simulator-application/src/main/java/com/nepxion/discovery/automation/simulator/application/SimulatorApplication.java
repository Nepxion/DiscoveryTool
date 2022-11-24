package com.nepxion.discovery.automation.simulator.application;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConditionRoute;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseProperty;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;

@SpringBootApplication
public class SimulatorApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SimulatorApplication.class, args);

        SimulatorTestCaseProperty testCaseProperty = applicationContext.getBean(SimulatorTestCaseProperty.class);
        SimulatorTestCaseCondition basicTestCaseCondition = (SimulatorTestCaseCondition) applicationContext.getBean("simulatorBasicTestCaseCondition");
        SimulatorTestCaseCondition releaseTestCaseCondition = (SimulatorTestCaseCondition) applicationContext.getBean("simulatorReleaseTestCaseCondition");
        SimulatorTestCaseConditionRoute releaseTestCaseConditionRoute = applicationContext.getBean(SimulatorTestCaseConditionRoute.class);
        SimulatorTestRunner testRunner = applicationContext.getBean(SimulatorTestRunner.class);
        try {
            SimulatorTestRunner.beforeTest();
            SimulatorTestStrategy testStrategy = testRunner.testInitialization(testCaseProperty, basicTestCaseCondition, releaseTestCaseCondition, releaseTestCaseConditionRoute);
            testRunner.testNormal(testStrategy);
            testRunner.testFirstVersionBasicRelease(testStrategy);
            testRunner.testFirstVersionBlueGreenGrayRelease(testStrategy);
            testRunner.testFirstResetRelease(testStrategy);
            testRunner.testSecondVersionBasicRelease(testStrategy);
            testRunner.testSecondVersionBlueGreenGrayRelease(testStrategy);
            testRunner.testSecondResetRelease(testStrategy);
            SimulatorTestRunner.afterTest();
            System.exit(0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}