package com.nepxion.discovery.automation.simulator.runner;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.number.OrderingComparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.nepxion.discovery.automation.common.logger.TestAssertLogger;
import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseEntity;
import com.nepxion.discovery.automation.simulator.strategy.SimulatorTestStrategy;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.util.PluginInfoUtil;

public class SimulatorTestCases {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorTestCases.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    // 测试无蓝绿灰度场景
    public void testNormal(SimulatorTestStrategy testStrategy) {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int blueGreenSampleCount = testCaseEntity.getBlueGreenSampleCount();

        Map<String, Integer> countMap = testInspection(testStrategy, blueGreenSampleCount, null, null, false, false);
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            LOG.info("侦测结果 : {} 命中次数={}", key, value);

            // 判断每个版本的服务实例都会被调用到
            TestAssertLogger.assertThat(LOG, "服务实例【" + key + "】的命中次数=" + value + ", 不符合预期", value, OrderingComparison.greaterThanOrEqualTo(1));
        }

        LOG.info("测试结果 : 通过");
    }

    // 测试蓝绿灰度兜底场景
    public void testBasic(SimulatorTestStrategy testStrategy) {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int blueGreenSampleCount = testCaseEntity.getBlueGreenSampleCount();
        List<String> oldVersionList = testStrategy.getOldVersionList();
        List<String> newVersionList = testStrategy.getNewVersionList();

        Map<String, Integer> countMap = testInspection(testStrategy, blueGreenSampleCount, null, null, true, false);
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            LOG.info("侦测结果 : {} 命中次数={}", key, value);

            // 新版本调用次数为0
            if (value == 0) {
                // 判断是否都调用新版本
                TestAssertLogger.assertEquals(LOG, "【新版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", newVersionList.contains(key), true);
            } else {
                // 判断是否都调用旧版本
                TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", oldVersionList.contains(key), true);
            }
        }

        LOG.info("测试结果 : 通过");
    }

    // 测试蓝绿场景
    public void testBlueGreen(SimulatorTestStrategy testStrategy, String headerName, String headerValue) {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int blueGreenSampleCount = testCaseEntity.getBlueGreenSampleCount();
        List<String> oldVersionList = testStrategy.getOldVersionList();
        List<String> newVersionList = testStrategy.getNewVersionList();
        List<String> blueParameter = testStrategy.getBlueParameter();

        Map<String, Integer> countMap = testInspection(testStrategy, blueGreenSampleCount, headerName, headerValue, true, false);
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            LOG.info("侦测结果 : {} 命中次数={}", key, value);

            if (StringUtils.equals(headerValue, blueParameter.get(1))) {
                // 旧版本调用次数为0
                if (value == 0) {
                    // 判断是否都调用旧版本
                    TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", oldVersionList.contains(key), true);
                } else {
                    // 判断是否都调用新版本
                    TestAssertLogger.assertEquals(LOG, "【新版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", newVersionList.contains(key), true);
                }
            } else {
                // 新版本调用次数为0
                if (value == 0) {
                    // 判断是否都调用新版本
                    TestAssertLogger.assertEquals(LOG, "【新版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", newVersionList.contains(key), true);
                } else {
                    // 判断是否都调用旧版本
                    TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，服务实例【" + key + "】未出现在调用结果列表, 不符合预期", oldVersionList.contains(key), true);
                }
            }
        }

        LOG.info("测试结果 : 通过");
    }

    // 测试灰度场景
    public void testGray(SimulatorTestStrategy testStrategy, String headerName, String headerValue) {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int graySampleCount = testCaseEntity.getGraySampleCount();
        int grayWeightOffset = testCaseEntity.getGrayWeightOffset();
        Map<String, List<String>> allVersionMap = testStrategy.getAllVersionMap();

        Map<String, Integer> countMap = testInspection(testStrategy, graySampleCount, headerName, headerValue, true, true);
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            LOG.info("侦测结果 : {} 命中次数={}", key, value);
        }

        List<String> grayParameter0 = testStrategy.getGrayParameter0();
        List<String> grayParameter1 = testStrategy.getGrayParameter1();
        List<Integer> grayWeight0 = testStrategy.getGrayWeight0();
        List<Integer> grayWeight1 = testStrategy.getGrayWeight1();

        int oldResultCount = -1;
        int newResultCount = -1;
        for (Map.Entry<String, List<String>> entry : allVersionMap.entrySet()) {
            String key = entry.getKey();
            List<String> versionList = entry.getValue();

            String oldVersion = versionList.get(0);
            int oldCount = countMap.get(key + SimulatorTestConstant.SEPARATE + oldVersion);

            // 新版本有多个，次数循环累加
            int newCount = 0;
            for (int i = 1; i < versionList.size(); i++) {
                String newVersion = versionList.get(i);
                newCount += countMap.get(key + SimulatorTestConstant.SEPARATE + newVersion);
            }

            // 判断全链路上每个服务新旧版本被命中的次数是否相等
            if (oldResultCount != -1) {
                TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，服务实例【" + key + "】与上一个服务实例调用次数不相等，不符合预期", oldResultCount, oldCount);
            }
            if (newResultCount != -1) {
                TestAssertLogger.assertEquals(LOG, "【新版本】链路中，服务实例【" + key + "】与上一个服务实例调用次数不相等，不符合预期", newResultCount, newCount);
            }

            oldResultCount = oldCount;
            newResultCount = newCount;
        }

        int oldDesireWeight = 0;
        int newDesireWeight = 0;
        if (StringUtils.isNotEmpty(headerValue)) {
            if (StringUtils.equals(headerValue, grayParameter0.get(1))) {
                oldDesireWeight = grayWeight0.get(0);
                newDesireWeight = grayWeight0.get(1);
            } else if (StringUtils.equals(headerValue, grayParameter1.get(1))) {
                oldDesireWeight = grayWeight1.get(0);
                newDesireWeight = grayWeight1.get(1);
            }
        } else {
            oldDesireWeight = 100;
            newDesireWeight = 0;
        }

        DecimalFormat format = new DecimalFormat("0.0000");
        double oldResultWeight = Double.valueOf(format.format((double) oldResultCount * 100 / graySampleCount));
        double newResultWeight = Double.valueOf(format.format((double) newResultCount * 100 / graySampleCount));

        LOG.info("权重结果偏差值={}%", grayWeightOffset);
        LOG.info("期望结果 : 旧版本路由权重={}%, 新版本路由权重={}%", oldDesireWeight, newDesireWeight);
        LOG.info("最终结果 : 旧版本路由权重={}%, 新版本路由权重={}%", oldResultWeight, newResultWeight);

        TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，路由权重结果超出设定值，不符合预期", oldResultWeight > oldDesireWeight - grayWeightOffset && oldResultWeight < oldDesireWeight + grayWeightOffset, true);
        TestAssertLogger.assertEquals(LOG, "【新版本】链路中，路由权重结果超出设定值，不符合预期", newResultWeight > newDesireWeight - grayWeightOffset && newResultWeight < newDesireWeight + grayWeightOffset, true);

        LOG.info("测试结果 : 通过");
    }

    private Map<String, Integer> testInspection(SimulatorTestStrategy testStrategy, int sampleCount, String headerName, String headerValue, boolean isolationAssert, boolean progressShown) {
        SimulatorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        String inspectUrl = testCaseEntity.getInspectUrl();

        LOG.info("侦测次数 : {}", sampleCount);

        InspectorEntity inspectorEntity = testStrategy.createInspectorEntity();

        Map<String, Integer> countMap = initializeCountMap(testStrategy);

        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotEmpty(headerName) && StringUtils.isNotEmpty(headerValue)) {
            headers.add(headerName, headerValue);
        }

        HttpEntity<InspectorEntity> requestEntity = new HttpEntity<InspectorEntity>(inspectorEntity, headers);
        try {
            for (int i = 0; i < sampleCount; i++) {
                String result = testRestTemplate.postForEntity(inspectUrl, requestEntity, InspectorEntity.class).getBody().getResult();

                TestUtil.determineRestException(testRestTemplate);

                if (progressShown) {
                    if ((i + 1) % 100 == 0) {
                        LOG.info("侦测进度 : 第{}次...", i + 1);
                    }
                }

                retrieveCountMap(testStrategy, countMap, result, isolationAssert);
            }
        } catch (Exception e) {
            LOG.error("侦测失败", e);

            throw e;
        }

        return countMap;
    }

    // 初始化每个服务版本的调用个数的Map，Key为serviceId + "@" + version，Value为0
    private Map<String, Integer> initializeCountMap(SimulatorTestStrategy testStrategy) {
        Map<String, Integer> countMap = new LinkedHashMap<String, Integer>();

        Map<String, List<String>> allVersionMap = testStrategy.getAllVersionMap();
        for (Map.Entry<String, List<String>> entry : allVersionMap.entrySet()) {
            String serviceId = entry.getKey();
            List<String> versionList = entry.getValue();
            for (String version : versionList) {
                countMap.put(serviceId + SimulatorTestConstant.SEPARATE + version, 0);
            }
        }

        return countMap;
    }

    // 计算每个服务版本的调用个数，Key为serviceId + "@" + version
    private void retrieveCountMap(SimulatorTestStrategy testStrategy, Map<String, Integer> countMap, String result, boolean isolationAssert) {
        List<String> versionResultList = retrieveVersionResultList(testStrategy, result, isolationAssert);
        for (String versionResult : versionResultList) {
            if (countMap.containsKey(versionResult)) {
                Integer count = countMap.get(versionResult);
                countMap.put(versionResult, count + 1);
            }
        }

        LOG.debug("侦测次数分析结果 : {}", countMap);
        LOG.debug("-------------------------------------------------");
    }

    // 处理每个调用结果，例如，[ID=discovery-guide-gateway][V=1.0] -> [ID=discovery-guide-service-a][V=1.0] -> [ID=discovery-guide-service-b][V=1.0]，产生serviceId + "@" + version结构的List
    private List<String> retrieveVersionResultList(SimulatorTestStrategy testStrategy, String result, boolean isolationAssert) {
        LOG.debug("------------------ Debug Result -----------------");
        LOG.debug("详细侦测结果 : {}", result);

        List<String> versionResultList = new ArrayList<String>();
        String[] array = result.split("->");
        for (int i = 0; i < array.length; i++) {
            String value = array[i];
            // i > 0表示要排除侦测入口
            if (i > 0) {
                String serviceId = PluginInfoUtil.substringSingle(value, SimulatorTestConstant.ID);
                String version = PluginInfoUtil.substringSingle(value, SimulatorTestConstant.V);

                if (serviceId != null && version != null) {
                    versionResultList.add(serviceId + SimulatorTestConstant.SEPARATE + version);
                }
            }
        }

        // 版本隔离的断言判断
        if (isolationAssert) {
            List<String> oldVersionList = testStrategy.getOldVersionList();
            List<String> newVersionList = testStrategy.getNewVersionList();

            // 如果第一个结果版本是在旧版本列表，那么所有的结果版本都应该在旧版本列表
            boolean oldVersionInvoked = oldVersionList.contains(versionResultList.get(0));
            // 如果第一个结果版本是在新版本列表，那么所有的结果版本都应该在新版本列表
            boolean newVersionInvoked = newVersionList.contains(versionResultList.get(0));

            for (String versionResult : versionResultList) {
                if (oldVersionInvoked) {
                    // 判断是否都调用旧版本
                    TestAssertLogger.assertEquals(LOG, "【旧版本】链路中，服务实例【" + versionResult + "】未与上一个服务实例处于同一个隔离区间, 不符合预期", oldVersionList.contains(versionResult), true);
                }
                if (newVersionInvoked) {
                    // 判断是否都调用新版本
                    TestAssertLogger.assertEquals(LOG, "【新版本】链路中，服务实例【" + versionResult + "】未与上一个服务实例处于同一个隔离区间, 不符合预期", newVersionList.contains(versionResult), true);
                }
            }
        }

        LOG.debug("侦测版本分析结果 : {}", versionResultList);

        return versionResultList;
    }
}