package com.nepxion.discovery.automation.simulator.data;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.util.StringUtil;

public class SimulatorTestCaseConditionDataResolver {
    public static List<Integer> getFinalTriggeredTestCaseGrayWeight(List<SimulatorTestCaseGrayConditionData> testCaseGrayConditionDataList, Map<String, String> parameter) {
        List<Integer> weight = getTriggeredTestCaseGrayWeight(testCaseGrayConditionDataList, parameter);
        if (weight == null) {
            weight = getBasicTestCaseGrayWeight(testCaseGrayConditionDataList);
        }

        return weight;
    }

    public static List<Integer> getTriggeredTestCaseGrayWeight(List<SimulatorTestCaseGrayConditionData> testCaseGrayConditionDataList, Map<String, String> parameter) {
        for (SimulatorTestCaseGrayConditionData testCaseGrayConditionData : testCaseGrayConditionDataList) {
            boolean isGrayExpressionTriggered = testCaseGrayConditionData.isTriggered(parameter);
            if (isGrayExpressionTriggered) {
                return testCaseGrayConditionData.getWeight();
            }
        }

        return null;
    }

    public static List<Integer> getBasicTestCaseGrayWeight(List<SimulatorTestCaseGrayConditionData> testCaseGrayConditionDataList) {
        for (SimulatorTestCaseGrayConditionData testCaseGrayConditionData : testCaseGrayConditionDataList) {
            String expression = testCaseGrayConditionData.getExpression();
            if (StringUtils.isEmpty(expression)) {
                return testCaseGrayConditionData.getWeight();
            }
        }

        return null;
    }

    public static void sort(List<? extends SimulatorTestCaseConditionData> testCaseConditionDataList) {
        // Header参数越多，越排在前面
        if (CollectionUtils.isNotEmpty(testCaseConditionDataList)) {
            Collections.sort(testCaseConditionDataList, new Comparator<SimulatorTestCaseConditionData>() {
                public int compare(SimulatorTestCaseConditionData testCaseConditionData1, SimulatorTestCaseConditionData testCaseConditionData2) {
                    Integer count1 = StringUtil.count(testCaseConditionData1.getExpression(), DiscoveryConstant.EXPRESSION_SUB_PREFIX);
                    Integer count2 = StringUtil.count(testCaseConditionData2.getExpression(), DiscoveryConstant.EXPRESSION_SUB_PREFIX);

                    return count2.compareTo(count1);
                }
            });
        }
    }
}