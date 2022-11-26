package com.nepxion.discovery.automation.simulator.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Value;

import com.nepxion.discovery.automation.common.entity.TestCaseProperty;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;

public class SimulatorTestCaseProperty extends TestCaseProperty implements SimulatorTestCaseEntity {
    @Value("${" + SimulatorTestConstant.TESTCASE_CONSOLE_URL + "}")
    private String consoleUrl;

    @Value("${" + SimulatorTestConstant.TESTCASE_CONSOLE_OPERATION_AWAIT_TIME + ":5000}")
    private Integer consoleOperationAwaitTime;

    @Value("${" + SimulatorTestConstant.TESTCASE_GROUP + "}")
    private String group;

    @Value("${" + SimulatorTestConstant.TESTCASE_SERVICE + "}")
    private String serviceId;

    @Value("${" + SimulatorTestConstant.TESTCASE_LOOP_COUNT + ":1}")
    private Integer loopCount;

    @Value("${" + SimulatorTestConstant.TESTCASE_BLUEGREEN_SAMPLE_COUNT + ":100}")
    private Integer blueGreenSampleCount;

    @Value("${" + SimulatorTestConstant.TESTCASE_GRAY_SAMPLE_COUNT + ":1000}")
    private Integer graySampleCount;

    @Value("${" + SimulatorTestConstant.TESTCASE_GRAY_WEIGHT_OFFSET + ":5}")
    private Integer grayWeightOffset;

    @Value("${" + SimulatorTestConstant.TESTCASE_VERSION_PREFER_ENABLED + ":false}")
    private Boolean versionPreferEnabled;

    @Value("${" + SimulatorTestConstant.TESTCASE_SECOND_RELEASE_ENABLED + ":true}")
    private Boolean secondReleaseEnabled;

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
}