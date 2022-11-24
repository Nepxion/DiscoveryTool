package com.nepxion.discovery.automation.simulator.runner;

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
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.nepxion.discovery.automation.common.runner.TestCaseContext;
import com.nepxion.discovery.automation.common.runner.TestCaseRunner;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConditionRoute;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseEntity;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.util.StringUtil;

public class SimulatorTestRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorTestRunner.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SimulatorTestCases testCases;

    private static long startTime;

    public static void beforeTest() {
        startTime = System.currentTimeMillis();
    }

    public static void afterTest() {
        LOG.info("* Finished all automation testcases in {} seconds", (System.currentTimeMillis() - startTime) / 1000);
    }

    public SimulatorTestStrategy testInitialization(String testCaseEntityContent, String basicTestCaseConditionContent, String releaseTestCaseConditionContent, String releaseTestCaseConditionRouteContent, boolean testCaseConfigWithYaml) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        SimulatorTestStrategy testStrategy = new TestCaseRunner<SimulatorTestStrategy>(testCaseContext) {
            @Override
            public SimulatorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntityContent, basicTestCaseConditionContent, releaseTestCaseConditionContent, releaseTestCaseConditionRouteContent, testCaseConfigWithYaml);
            }
        }.start();

        testStrategy.setTestCaseContext(testCaseContext);

        return testStrategy;
    }

    public SimulatorTestStrategy testInitialization(SimulatorTestCaseEntity testCaseEntity, SimulatorTestCaseCondition basicTestCaseCondition, SimulatorTestCaseCondition releaseTestCaseCondition, SimulatorTestCaseConditionRoute releaseTestCaseConditionRoute) throws Exception {
        TestCaseContext testCaseContext = new TestCaseContext();

        SimulatorTestStrategy testStrategy = new TestCaseRunner<SimulatorTestStrategy>(testCaseContext) {
            @Override
            public SimulatorTestStrategy run() throws Exception {
                return testInitialization(0, testCaseEntity, basicTestCaseCondition, releaseTestCaseCondition, releaseTestCaseConditionRoute);
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
        String basicTestCaseConditionContent = testStrategy.getBasicTestCaseConditionContent();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testVersionBasicRelease(2, 1, basicTestCaseConditionContent != null ? basicTestCaseConditionContent : SimulatorTestCaseCondition.getBasicFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testFirstVersionBlueGreenGrayRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String releaseTestCaseConditionContent = testStrategy.getReleaseTestCaseConditionContent();

        new TestCaseRunner<Void>(testCaseContext) {
            @Override
            public Void run() throws Exception {
                testVersionBlueGreenGrayRelease(3, 1, releaseTestCaseConditionContent != null ? releaseTestCaseConditionContent : SimulatorTestCaseCondition.getReleaseFile(), testStrategy);

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
        String basicTestCaseConditionContent = testStrategy.getBasicTestCaseConditionContent();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        boolean secondReleaseEnabled = testCaseEntity.isSecondReleaseEnabled();

        new TestCaseRunner<Void>(testCaseContext, secondReleaseEnabled) {
            @Override
            public Void run() throws Exception {
                testVersionBasicRelease(5, 2, basicTestCaseConditionContent != null ? basicTestCaseConditionContent : SimulatorTestCaseConditionRoute.getBasicFile(), testStrategy);

                return null;
            }
        }.start();
    }

    public void testSecondVersionBlueGreenGrayRelease(SimulatorTestStrategy testStrategy) throws Exception {
        TestCaseContext testCaseContext = testStrategy.getTestCaseContext();
        String releaseTestCaseConditionRouteContent = testStrategy.getReleaseTestCaseConditionRouteContent();
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        boolean secondReleaseEnabled = testCaseEntity.isSecondReleaseEnabled();

        new TestCaseRunner<Void>(testCaseContext, secondReleaseEnabled) {
            @Override
            public Void run() throws Exception {
                testVersionBlueGreenGrayRelease(6, 2, releaseTestCaseConditionRouteContent != null ? releaseTestCaseConditionRouteContent : SimulatorTestCaseConditionRoute.getReleaseFile(), testStrategy);

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

    public SimulatorTestStrategy testInitialization(int sceneIndex, String testCaseEntityContent, String basicTestCaseConditionContent, String releaseTestCaseConditionContent, String releaseTestCaseConditionRouteContent, boolean testCaseConfigWithYaml) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】初始化上下文...", sceneIndex);
        long startTime = System.currentTimeMillis();
        SimulatorTestStrategy testStrategy = new SimulatorTestStrategy();
        testStrategy.setTestRestTemplate(testRestTemplate);
        testStrategy.testInitialization(testCaseEntityContent, basicTestCaseConditionContent, releaseTestCaseConditionContent, releaseTestCaseConditionRouteContent, testCaseConfigWithYaml);
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);

        return testStrategy;
    }

    public SimulatorTestStrategy testInitialization(int sceneIndex, SimulatorTestCaseEntity testCaseEntity, SimulatorTestCaseCondition basicTestCaseCondition, SimulatorTestCaseCondition releaseTestCaseCondition, SimulatorTestCaseConditionRoute releaseTestCaseConditionRoute) throws Exception {
        LOG.info("-------------------------------------------------");

        LOG.info("【模拟场景{}】初始化上下文...", sceneIndex);
        long startTime = System.currentTimeMillis();
        SimulatorTestStrategy testStrategy = new SimulatorTestStrategy();
        testStrategy.setTestRestTemplate(testRestTemplate);
        testStrategy.testInitialization(testCaseEntity, basicTestCaseCondition, releaseTestCaseCondition, releaseTestCaseConditionRoute);
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);

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

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);
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

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);
    }

    public void testVersionBlueGreenGrayRelease(int sceneIndex, int releaseIndex, String input, SimulatorTestStrategy testStrategy) throws Exception {
        LOG.info("-------------------------------------------------");

        List<String> greenParameter = testStrategy.getGreenParameter();
        List<String> blueParameter = testStrategy.getBlueParameter();
        List<String> grayParameter0 = testStrategy.getGrayParameter0();
        List<String> grayParameter1 = testStrategy.getGrayParameter1();
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

        LOG.info("【模拟场景{}】蓝绿策略，测试全链路侦测，Header : {}...", sceneIndex, "无");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testBlueGreen(testStrategy, null, null);
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】蓝绿策略，测试全链路侦测，Header : {}...", sceneIndex, StringUtil.convertToString(greenParameter, DiscoveryConstant.EQUALS));
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testBlueGreen(testStrategy, greenParameter.get(0), greenParameter.get(1));
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】蓝绿策略，测试全链路侦测，Header : {}...", sceneIndex, StringUtil.convertToString(blueParameter, DiscoveryConstant.EQUALS));
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testBlueGreen(testStrategy, blueParameter.get(0), blueParameter.get(1));
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】灰度策略，测试全链路侦测，Header : {}...", sceneIndex, "无");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testGray(testStrategy, null, null);
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】灰度策略，测试全链路侦测，Header : {}...", sceneIndex, StringUtil.convertToString(grayParameter0, DiscoveryConstant.EQUALS));
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testGray(testStrategy, grayParameter0.get(0), grayParameter0.get(1));
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】灰度策略，测试全链路侦测，Header : {}...", sceneIndex, StringUtil.convertToString(grayParameter1, DiscoveryConstant.EQUALS));
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loopCount; i++) {
            testCases.testGray(testStrategy, grayParameter1.get(0), grayParameter1.get(1));
        }
        LOG.info("测试耗时 : {} 秒", (System.currentTimeMillis() - startTime) / 1000);

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);
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

        LOG.info("【模拟场景{}】* 测试通过...", sceneIndex);
    }
}