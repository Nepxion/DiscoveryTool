package com.nepxion.discovery.automation.inspector.console.resource;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.common.console.resource.ConsoleResourceImpl;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

public class InspectorConsoleResourceImpl extends ConsoleResourceImpl implements InspectorConsoleResource {
    @Autowired
    private InspectorTestRunner testRunner;

    @Override
    public String getTestName() {
        return "Inspector";
    }

    @Override
    public int getTestConfigPartsCount() {
        return 2;
    }

    @Override
    public void validateTest(List<String> testConfigList, boolean testCaseConfigWithYaml) {

    }

    @Override
    public void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);
        String testCaseCondition = testConfigList.get(1);

        InspectorTestRunner.beforeTest();
        InspectorTestStrategy testStrategy = testRunner.testInitialization(testCaseConfig, testCaseCondition, testCaseConfigWithYaml);
        testRunner.testInspection(testStrategy);
        InspectorTestRunner.afterTest();
    }

    @Override
    public void finishTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {

    }
}