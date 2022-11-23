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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.common.console.resource.ConsoleResourceImpl;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;

public class InspectorConsoleResourceImpl extends ConsoleResourceImpl implements InspectorConsoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(InspectorConsoleResourceImpl.class);

    @Autowired
    private InspectorTestRunner testRunner;

    @Override
    public String getTestThreadNamePrefix() {
        return "Inspector-Thread-";
    }

    @Override
    public int getTestConfigPartsCount() {
        return 2;
    }

    @Override
    public void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) {
        String testCaseConfig = testConfigList.get(0);
        String testCaseCondition = testConfigList.get(1);

        try {
            InspectorTestRunner.beforeTest();
            InspectorTestStrategy testStrategy = testRunner.testInitialization(testCaseConfig, testCaseCondition, testCaseConfigWithYaml);
            testRunner.testInspection(testStrategy);
            InspectorTestRunner.afterTest();
        } catch (Exception e) {
            LOG.error("Inspector test failed", e);

            e.printStackTrace();
        }
    }
}