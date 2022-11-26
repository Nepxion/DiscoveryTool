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

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.automation.common.console.resource.ConsoleResourceImpl;
import com.nepxion.discovery.automation.simulator.console.lock.SimulatorConsoleLock;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConfig;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;
import com.nepxion.discovery.common.exception.DiscoveryException;

public class SimulatorConsoleResourceImpl extends ConsoleResourceImpl implements SimulatorConsoleResource {
    @Autowired
    private SimulatorTestRunner testRunner;

    @Autowired
    private SimulatorConsoleLock consoleLock;

    @Override
    public String getTestName() {
        return "Simulator";
    }

    @Override
    public int getTestConfigPartsCount() {
        return 4;
    }

    @Override
    public void validateTest(List<String> testConfigList, boolean testCaseConfigWithYaml) {
        String testCaseConfig = testConfigList.get(0);

        try {
            String key = getKey(testCaseConfig, testCaseConfigWithYaml);
            if (consoleLock.isLocked(key)) {
                throw new DiscoveryException("Testcase Task 【" + key + "】 is running now");
            }
        } catch (Exception e) {
            throw new DiscoveryException(e);
        }
    }

    @Override
    public void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);
        String testCaseReleaseBasicCondition = testConfigList.get(1);
        String testCaseReleaseFirstCondition = testConfigList.get(2);
        String testCaseReleaseSecondCondition = testConfigList.get(3);

        String key = getKey(testCaseConfig, testCaseConfigWithYaml);
        consoleLock.lock(key);

        SimulatorTestStrategy testStrategy = testRunner.testInitialization(testCaseConfig, testCaseReleaseBasicCondition, testCaseReleaseFirstCondition, testCaseReleaseSecondCondition, testCaseConfigWithYaml);
        testRunner.testNormal(testStrategy);
        testRunner.testFirstVersionBasicRelease(testStrategy);
        testRunner.testFirstVersionBlueGreenGrayRelease(testStrategy);
        testRunner.testFirstResetRelease(testStrategy);
        testRunner.testSecondVersionBasicRelease(testStrategy);
        testRunner.testSecondVersionBlueGreenGrayRelease(testStrategy);
        testRunner.testSecondResetRelease(testStrategy);
        testRunner.afterTest(testStrategy);
    }

    @Override
    public void finishTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);

        String key = getKey(testCaseConfig, testCaseConfigWithYaml);
        consoleLock.unlock(key);
    }

    private String getKey(String testCaseConfig, boolean testCaseConfigWithYaml) throws Exception {
        SimulatorTestCaseConfig simulatorTestCaseConfig = SimulatorTestCaseConfig.fromText(testCaseConfig, testCaseConfigWithYaml);

        String group = simulatorTestCaseConfig.getGroup();
        String serviceId = simulatorTestCaseConfig.getServiceId();

        return group + SimulatorTestConstant.SEPARATE + serviceId;
    }
}