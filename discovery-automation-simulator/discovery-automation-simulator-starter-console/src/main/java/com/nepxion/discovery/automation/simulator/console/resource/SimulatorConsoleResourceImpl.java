package com.nepxion.discovery.automation.simulator.console.resource;

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
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;

public class SimulatorConsoleResourceImpl extends ConsoleResourceImpl implements SimulatorConsoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorConsoleResourceImpl.class);

    @Autowired
    private SimulatorTestRunner testRunner;

    @Override
    public String getTestThreadNamePrefix() {
        return "Simulator-Thread-";
    }

    @Override
    public int getTestConfigPartsCount() {
        return 4;
    }

    @Override
    public void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) {
        String testCaseConfig = testConfigList.get(0);
        String basicTestCaseCondition = testConfigList.get(1);
        String releaseTestCaseCondition = testConfigList.get(2);
        String releaseTestCaseConditionRoute = testConfigList.get(3);

        try {
            SimulatorTestRunner.beforeTest();
            SimulatorTestStrategy testStrategy = testRunner.testInitialization(testCaseConfig, basicTestCaseCondition, releaseTestCaseCondition, releaseTestCaseConditionRoute, testCaseConfigWithYaml);
            testRunner.testNormal(testStrategy);
            testRunner.testFirstVersionBasicRelease(testStrategy);
            testRunner.testFirstVersionBlueGreenGrayRelease(testStrategy);
            testRunner.testFirstResetRelease(testStrategy);
            testRunner.testSecondVersionBasicRelease(testStrategy);
            testRunner.testSecondVersionBlueGreenGrayRelease(testStrategy);
            testRunner.testSecondResetRelease(testStrategy);
            SimulatorTestRunner.afterTest();
        } catch (Exception e) {
            LOG.error("Simulator test failed", e);

            e.printStackTrace();
        }
    }
}