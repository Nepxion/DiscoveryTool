package com.nepxion.discovery.automation.simulator.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.automation.common.entity.TestCaseConfig;
import com.nepxion.discovery.automation.common.util.TestConfigurationUtil;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;

public class SimulatorTestCaseConfig extends TestCaseConfig implements SimulatorTestCaseEntity {
    private static final long serialVersionUID = 7396811350368043499L;

    private String consoleUrl;
    private int consoleOperationAwaitTime = 5000;
    private String group;
    private String serviceId;
    private int loopCount = 1;
    private int blueGreenSampleCount = 100;
    private int graySampleCount = 1000;
    private int grayWeightOffset = 5;
    private boolean versionPreferEnabled = false;
    private boolean secondReleaseEnabled = true;

    @Override
    public String getConsoleUrl() {
        return consoleUrl;
    }

    @Override
    public void setConsoleUrl(String consoleUrl) {
        this.consoleUrl = consoleUrl;
    }

    @Override
    public int getConsoleOperationAwaitTime() {
        return consoleOperationAwaitTime;
    }

    @Override
    public void setConsoleOperationAwaitTime(int consoleOperationAwaitTime) {
        this.consoleOperationAwaitTime = consoleOperationAwaitTime;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int getLoopCount() {
        return loopCount;
    }

    @Override
    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    @Override
    public int getBlueGreenSampleCount() {
        return blueGreenSampleCount;
    }

    @Override
    public void setBlueGreenSampleCount(int blueGreenSampleCount) {
        this.blueGreenSampleCount = blueGreenSampleCount;
    }

    @Override
    public int getGraySampleCount() {
        return graySampleCount;
    }

    @Override
    public void setGraySampleCount(int graySampleCount) {
        this.graySampleCount = graySampleCount;
    }

    @Override
    public int getGrayWeightOffset() {
        return grayWeightOffset;
    }

    @Override
    public void setGrayWeightOffset(int grayWeightOffset) {
        this.grayWeightOffset = grayWeightOffset;
    }

    @Override
    public boolean isVersionPreferEnabled() {
        return versionPreferEnabled;
    }

    @Override
    public void setVersionPreferEnabled(boolean versionPreferEnabled) {
        this.versionPreferEnabled = versionPreferEnabled;
    }

    @Override
    public boolean isSecondReleaseEnabled() {
        return secondReleaseEnabled;
    }

    @Override
    public void setSecondReleaseEnabled(boolean secondReleaseEnabled) {
        this.secondReleaseEnabled = secondReleaseEnabled;
    }

    public static SimulatorTestCaseConfig fromText(String input, boolean testCaseConfigWithYaml) throws Exception {
        return testCaseConfigWithYaml ? fromYamlText(input) : fromPropertiesText(input);
    }

    public static SimulatorTestCaseConfig fromYamlText(String input) throws Exception {
        Map<String, String> map = null;
        try {
            map = TestConfigurationUtil.parseYaml(input);
        } catch (Exception e) {
            throw e;
        }

        return fromMap(map);
    }

    public static SimulatorTestCaseConfig fromPropertiesText(String input) throws Exception {
        Map<String, String> map = null;
        try {
            map = TestConfigurationUtil.parseProperties(input);
        } catch (Exception e) {
            throw e;
        }

        return fromMap(map);
    }

    public static SimulatorTestCaseConfig fromMap(Map<String, String> map) {
        SimulatorTestCaseConfig testCaseConfig = new SimulatorTestCaseConfig();

        String consoleUrl = map.get(SimulatorTestConstant.TESTCASE_CONSOLE_URL);
        if (StringUtils.isNotBlank(consoleUrl)) {
            testCaseConfig.setConsoleUrl(consoleUrl);
        }

        String consoleOperationAwaitTime = map.get(SimulatorTestConstant.TESTCASE_CONSOLE_OPERATION_AWAIT_TIME);
        if (StringUtils.isNotBlank(consoleOperationAwaitTime)) {
            testCaseConfig.setConsoleOperationAwaitTime(Integer.valueOf(consoleOperationAwaitTime));
        }

        String group = map.get(SimulatorTestConstant.TESTCASE_GROUP);
        if (StringUtils.isNotBlank(group)) {
            testCaseConfig.setGroup(group);
        }

        String serviceId = map.get(SimulatorTestConstant.TESTCASE_SERVICE);
        if (StringUtils.isNotBlank(serviceId)) {
            testCaseConfig.setServiceId(serviceId);
        }

        String inspectUrl = map.get(SimulatorTestConstant.TESTCASE_INSPECT_URL);
        if (StringUtils.isNotBlank(inspectUrl)) {
            testCaseConfig.setInspectUrl(inspectUrl);
        }

        String inspectContextService = map.get(SimulatorTestConstant.TESTCASE_INSPECT_CONTEXT_SERVICE);
        if (StringUtils.isNotBlank(inspectContextService)) {
            testCaseConfig.setInspectContextService(inspectContextService);
        }

        String loopCount = map.get(SimulatorTestConstant.TESTCASE_LOOP_COUNT);
        if (StringUtils.isNotBlank(loopCount)) {
            testCaseConfig.setLoopCount(Integer.valueOf(loopCount));
        }

        String blueGreenSampleCount = map.get(SimulatorTestConstant.TESTCASE_BLUEGREEN_SAMPLE_COUNT);
        if (StringUtils.isNotBlank(blueGreenSampleCount)) {
            testCaseConfig.setBlueGreenSampleCount(Integer.valueOf(blueGreenSampleCount));
        }

        String graySampleCount = map.get(SimulatorTestConstant.TESTCASE_GRAY_SAMPLE_COUNT);
        if (StringUtils.isNotBlank(graySampleCount)) {
            testCaseConfig.setGraySampleCount(Integer.valueOf(graySampleCount));
        }

        String grayWeightOffset = map.get(SimulatorTestConstant.TESTCASE_GRAY_WEIGHT_OFFSET);
        if (StringUtils.isNotBlank(grayWeightOffset)) {
            testCaseConfig.setGrayWeightOffset(Integer.valueOf(grayWeightOffset));
        }

        String versionPreferEnabled = map.get(SimulatorTestConstant.TESTCASE_VERSION_PREFER_ENABLED);
        if (StringUtils.isNotBlank(versionPreferEnabled)) {
            testCaseConfig.setVersionPreferEnabled(Boolean.valueOf(versionPreferEnabled));
        }

        String secondReleaseEnabled = map.get(SimulatorTestConstant.TESTCASE_SECOND_RELEASE_ENABLED);
        if (StringUtils.isNotBlank(secondReleaseEnabled)) {
            testCaseConfig.setSecondReleaseEnabled(Boolean.valueOf(secondReleaseEnabled));
        }

        String debugEnabled = map.get(SimulatorTestConstant.TESTCASE_DEBUG_ENABLED);
        if (StringUtils.isNotBlank(debugEnabled)) {
            testCaseConfig.setDebugEnabled(Boolean.valueOf(debugEnabled));
        }

        return testCaseConfig;
    }
}