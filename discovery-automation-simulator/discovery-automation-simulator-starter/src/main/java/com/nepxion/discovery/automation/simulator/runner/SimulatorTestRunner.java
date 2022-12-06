package com.nepxion.discovery.automation.simulator.runner;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.nepxion.discovery.automation.common.runner.TestCaseContext;
import com.nepxion.discovery.automation.common.runner.TestCaseRunner;
import com.nepxion.discovery.automation.common.runner.TestRunner;
import com.nepxion.discovery.automation.simulator.data.SimulatorTestCaseConditionData;
import com.nepxion.discovery.automation.simulator.data.SimulatorTestCaseConditionDataResolver;
import com.nepxion.discovery.automation.simulator.data.SimulatorTestCaseGrayConditionData;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseEntity;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseBasicCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseFirstCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseSecondCondition;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;

public class SimulatorTestRunner extends TestRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorTestRunner.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SimulatorTestCases testCases;

    public SimulatorTestStrategy testInitialization(String testCaseEntityContent, String testCaseReleaseBasicConditionContent, String testCaseReleaseFirstConditionContent, String testCaseReleaseSecondConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        SimulatorTestStrategy testStrategy = new TestCaseRunner<SimulatorTestStrategy>(testCaseContext) {
            @Override
            public SimulatorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntityContent, testCaseReleaseBasicConditionContent, testCaseReleaseFirstConditionContent, testCaseReleaseSecondConditionContent, testCaseConfigWithYaml);
            }
        }.start();

        testStrategy.setTestCaseContext(testCaseContext);

        return testStrategy;
    }

    public SimulatorTestStrategy testInitialization(SimulatorTestCaseEntity testCaseEntity, SimulatorTestCaseReleaseBasicCondition testCaseReleaseBasicCondition, SimulatorTestCaseReleaseFirstCondition testCaseReleaseFirstCondition, SimulatorTestCaseReleaseSecondCondition testCaseReleaseSecondCondition) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        SimulatorTestStrategy testStrategy = new TestCaseRunner<SimulatorTestStrategy>(testCaseContext) {
            @Override
            public SimulatorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntity, testCaseReleaseBasicCondition, testCaseReleaseFirstCondition, testCaseReleaseSecondCondition);
            }
        }.start();

        testStrategy.setTestCaseContext(testCaseContext);

        return testStrategy;
    }

    public void testNormal(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testNormal(1, testStrategy);

                return null;
            }
        }.start();
    }

    public void testFirstVersionBasicRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String testCaseReleaseBasicConditionContent = testStrategy.getTestCaseReleaseBasicConditionContent();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testVersionBasicRelease(2, 1, testCaseReleaseBasicConditionContent != null ? testCaseReleaseBasicConditionContent : SimulatorTestCaseReleaseBasicCondition.getFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testFirstVersionBlueGreenGrayRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String testCaseReleaseFirstConditionContent = testStrategy.getTestCaseReleaseFirstConditionContent();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testVersionBlueGreenGrayRelease(3, 1, testCaseReleaseFirstConditionContent != null ? testCaseReleaseFirstConditionContent : SimulatorTestCaseReleaseFirstCondition.getFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testFirstResetRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testResetRelease(4, 1, testStrategy);

                return null;
            }
        }.start();
    }

    public void testSecondVersionBasicRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String testCaseReleaseBasicConditionContent = testStrategy.getTestCaseReleaseBasicConditionContent();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        boolean secondReleaseEnabled = testCaseEntity.isSecondReleaseEnabled();

        new TestCaseRunner<Void>(testCaseContext, secondReleaseEnabled) {
            @Override
            public Void run() throws Exception {
                testVersionBasicRelease(5, 2, testCaseReleaseBasicConditionContent != null ? testCaseReleaseBasicConditionContent : SimulatorTestCaseReleaseBasicCondition.getFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testSecondVersionBlueGreenGrayRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String testCaseReleaseSecondConditionContent = testStrategy.getTestCaseReleaseSecondConditionContent();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        boolean secondReleaseEnabled = testCaseEntity.isSecondReleaseEnabled();

        new TestCaseRunner<Void>(testCaseContext, secondReleaseEnabled) {
            @Override
            public Void run() throws Exception {
                testVersionBlueGreenGrayRelease(6, 2, testCaseReleaseSecondConditionContent != null ? testCaseReleaseSecondConditionContent : SimulatorTestCaseReleaseSecondCondition.getFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testSecondResetRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        boolean secondReleaseEnabled = testCaseEntity.isSecondReleaseEnabled();

        new TestCaseRunner<Void>(testCaseContext, secondReleaseEnabled) {
            @Override
            public Void run() throws Exception {
                testResetRelease(7, 2, testStrategy);

                return null;
            }
        }.start();
    }

    public SimulatorTestStrategy testInitialization(int sceneIndex, String testCaseEntityContent, String testCaseReleaseBasicConditionContent, String testCaseReleaseFirstConditionContent, String testCaseReleaseSecondConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】初始化上下文...", sceneIndex);

        long startTime = System.currentTimeMillis();
        SimulatorTestStrategy testStrategy = new SimulatorTestStrategy();
        testStrategy.setTestRestTemplate(testRestTemplate);
        testStrategy.testInitialization(testCaseEntityContent, testCaseReleaseBasicConditionContent, testCaseReleaseFirstConditionContent, testCaseReleaseSecondConditionContent, testCaseConfigWithYaml);

        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);

        return testStrategy;
    }

    public SimulatorTestStrategy testInitialization(int sceneIndex, SimulatorTestCaseEntity testCaseEntity, SimulatorTestCaseReleaseBasicCondition testCaseReleaseBasicCondition, SimulatorTestCaseReleaseFirstCondition testCaseReleaseFirstCondition, SimulatorTestCaseReleaseSecondCondition testCaseReleaseSecondCondition) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】初始化上下文...", sceneIndex);

        long startTime = System.currentTimeMillis();
        SimulatorTestStrategy testStrategy = new SimulatorTestStrategy();
        testStrategy.setTestRestTemplate(testRestTemplate);
        testStrategy.testInitialization(testCaseEntity, testCaseReleaseBasicCondition, testCaseReleaseFirstCondition, testCaseReleaseSecondCondition);

        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);

        return testStrategy;
    }

    public void testNormal(int sceneIndex, SimulatorTestStrategy testStrategy) throws Exception {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int loopCount = testCaseEntity.getLoopCount();
        boolean versionPreferEnabled = testCaseEntity.isVersionPreferEnabled();

        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】无蓝绿灰度发布场景...", sceneIndex);

        LOG.info("【模拟场景{}】清除蓝绿灰度发布策略...", sceneIndex);

        testStrategy.resetRelease();

        LOG.info("【模拟场景{}】测试全链路侦测...", sceneIndex);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            if (versionPreferEnabled) {
                // 版本偏好模式，用兜底路由测试
                testCases.testBasic(testStrategy);
            } else {
                // 非版本偏好模式，用普通路由测试
                testCases.testNormal(testStrategy);
            }
        }

        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);
    }

    public void testVersionBasicRelease(int sceneIndex, int releaseIndex, String input, SimulatorTestStrategy testStrategy) throws Exception {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int loopCount = testCaseEntity.getLoopCount();

        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】第{}次蓝绿灰度发布场景，启动兜底策略...", sceneIndex, releaseIndex);

        if (releaseIndex == 1) {
            // 第一次蓝绿灰度兜底策略，用创建方式
            testStrategy.createVersionRelease(input);
        } else if (releaseIndex == 2) {
            // 第二次蓝绿灰度兜底策略，用重建方式
            testStrategy.recreateVersionRelease(input);
        }

        LOG.info("【模拟场景{}】兜底策略，测试全链路侦测...", sceneIndex);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testBasic(testStrategy);
        }

        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);
    }

    public void testVersionBlueGreenGrayRelease(int sceneIndex, int releaseIndex, String input, SimulatorTestStrategy testStrategy) throws Exception {
        LOG.info("-------------------------------------------------");

        boolean hasBlueGreen = testStrategy.hasBlueGreen();
        boolean hasGray = testStrategy.hasGray();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int loopCount = testCaseEntity.getLoopCount();

        LOG.info("【模拟场景{}】第{}次蓝绿灰度发布场景，启动蓝绿灰度发布策略...", sceneIndex, releaseIndex);

        if (releaseIndex == 1) {
            // 第一次蓝绿灰度发布策略，用创建方式
            testStrategy.createVersionRelease(input);
        } else if (releaseIndex == 2) {
            // 第二次蓝绿灰度发布策略，用重建方式
            testStrategy.recreateVersionRelease(input);
        }

        if (hasBlueGreen) {
            LOG.info("【模拟场景{}】蓝绿策略，测试全链路侦测，Header : {}...", sceneIndex, "无");

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                testCases.testBlueGreen(testStrategy, null);
            }

            LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

            List<SimulatorTestCaseConditionData> testCaseBlueGreenConditionDataList = Arrays.asList(testStrategy.getTestCaseGreenConditionData(), testStrategy.getTestCaseBlueConditionData());
            SimulatorTestCaseConditionDataResolver.sort(testCaseBlueGreenConditionDataList);
            for (SimulatorTestCaseConditionData testCaseConditionData : testCaseBlueGreenConditionDataList) {
                Map<String, String> blueGreenParameter = testCaseConditionData.getParameter();

                LOG.info("【模拟场景{}】蓝绿策略，测试全链路侦测，Header : {}...", sceneIndex, MapUtils.isNotEmpty(blueGreenParameter) ? blueGreenParameter : "无");

                startTime = System.currentTimeMillis();
                for (int i = 0; i < loopCount; i++) {
                    testCases.testBlueGreen(testStrategy, blueGreenParameter);
                }
                LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);
            }
        }

        if (hasGray) {
            LOG.info("【模拟场景{}】灰度策略，测试全链路侦测，Header : {}...", sceneIndex, "无");

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < loopCount; i++) {
                testCases.testGray(testStrategy, null);
            }
            LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

            List<SimulatorTestCaseGrayConditionData> testCaseGrayConditionDataList = testStrategy.getTestCaseGrayConditionDataList();
            SimulatorTestCaseConditionDataResolver.sort(testCaseGrayConditionDataList);
            for (SimulatorTestCaseGrayConditionData testCaseGrayConditionData : testCaseGrayConditionDataList) {
                Map<String, String> grayParameter = testCaseGrayConditionData.getParameter();

                LOG.info("【模拟场景{}】灰度策略，测试全链路侦测，Header : {}...", sceneIndex, MapUtils.isNotEmpty(grayParameter) ? grayParameter : "无");

                startTime = System.currentTimeMillis();
                for (int i = 0; i < loopCount; i++) {
                    testCases.testGray(testStrategy, grayParameter);
                }
                LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);
            }
        }

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);
    }

    public void testResetRelease(int sceneIndex, int releaseIndex, SimulatorTestStrategy testStrategy) throws Exception {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int loopCount = testCaseEntity.getLoopCount();
        boolean versionPreferEnabled = testCaseEntity.isVersionPreferEnabled();

        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】第{}次蓝绿灰度发布场景，重置并停止蓝绿灰度发布策略...", sceneIndex, releaseIndex);

        testStrategy.resetRelease();

        LOG.info("【模拟场景{}】测试全链路侦测...", sceneIndex);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            if (versionPreferEnabled) {
                // 版本偏好模式，用兜底路由测试
                testCases.testBasic(testStrategy);
            } else {
                // 非版本偏好模式，用普通路由测试
                testCases.testNormal(testStrategy);
            }
        }

        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        testHighlightGreen("测试结果 : * 通过");

        LOG.info("【模拟场景{}】结束", sceneIndex);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}