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
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConfig;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.lock.DiscoveryLock;

public class SimulatorConsoleResourceImpl extends ConsoleResourceImpl implements SimulatorConsoleResource {
    @Autowired
    private SimulatorTestRunner testRunner;

    @Autowired
    private DiscoveryLock lock;

    @Override
    public String getTestName() {
        return "Simulator";
    }

    @Override
    public int getTestConfigPartsCount() {
        return 4;
    }

    @Override
    public void beforeTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);

        String key = getKey(testCaseConfig, testCaseConfigWithYaml);

        // 通过线程安全的锁组件（本地锁或者分布式锁）并行控制测试用例，根据Key（group@serviceId）进行判断，不允许有多个Key相同的测试用例同时运行
        // 新发起的测试用例尝试获取锁。如果获取不到，则结束并抛出异常
        if (!lock.tryLock(key)) {
            throw new DiscoveryException("自动化测试任务【" + key + "】正在执行中，不能同时并发执行相同的任务");
        }
    }

    @Override
    public void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);
        String testCaseReleaseBasicCondition = testConfigList.get(1);
        String testCaseReleaseFirstCondition = testConfigList.get(2);
        String testCaseReleaseSecondCondition = testConfigList.get(3);

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
    public void afterTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception {
        String testCaseConfig = testConfigList.get(0);

        String key = getKey(testCaseConfig, testCaseConfigWithYaml);

        // 测试用例结束或者中途抛错释放锁
        lock.unlock(key);
    }

    @Override
    public List<String> getRunningTestCases() {
        // 获取当前正在运行的测试用例列表
        return lock.getHeldLocks();
    }

    private String getKey(String testCaseConfig, boolean testCaseConfigWithYaml) throws Exception {
        SimulatorTestCaseConfig simulatorTestCaseConfig = SimulatorTestCaseConfig.fromText(testCaseConfig, testCaseConfigWithYaml);

        String group = simulatorTestCaseConfig.getGroup();
        String serviceId = simulatorTestCaseConfig.getServiceId();

        return group + SimulatorTestConstant.SEPARATE + serviceId;
    }
}