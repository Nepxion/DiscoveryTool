package com.nepxion.discovery.automation.inspector.application;

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

import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseProperty;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

@SpringBootApplication
public class InspectorApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(InspectorApplication.class, args);

        InspectorTestCaseProperty testCaseProperty = applicationContext.getBean(InspectorTestCaseProperty.class);
        InspectorTestCaseCondition testCaseCondition = applicationContext.getBean(InspectorTestCaseCondition.class);
        InspectorTestRunner testRunner = applicationContext.getBean(InspectorTestRunner.class);
        try {
            InspectorTestStrategy testStrategy = testRunner.testInitialization(testCaseProperty, testCaseCondition);
            testRunner.testInspection(testStrategy);
            testRunner.afterTest(testStrategy);
            System.exit(0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}