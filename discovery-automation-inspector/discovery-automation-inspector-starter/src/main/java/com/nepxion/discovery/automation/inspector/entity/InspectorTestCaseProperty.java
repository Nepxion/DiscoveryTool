package com.nepxion.discovery.automation.inspector.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Value;

import com.nepxion.discovery.automation.inspector.constant.InspectorTestConstant;

public class InspectorTestCaseProperty implements InspectorTestCaseEntity {
    @Value("${" + InspectorTestConstant.TESTCASE_INSPECT_URL + "}")
    private String inspectUrl;

    @Value("${" + InspectorTestConstant.TESTCASE_INSPECT_CONTEXT_SERVICE + ":}")
    private String inspectContextService;

    @Value("${" + InspectorTestConstant.TESTCASE_SAMPLE_COUNT + ":10}")
    private Integer sampleCount;

    @Value("${" + InspectorTestConstant.TESTCASE_RESULT_FILTER + ":" + InspectorTestConstant.ID + "," + InspectorTestConstant.V + "}")
    private String resultFilter;

    @Value("${" + InspectorTestConstant.TESTCASE_DEBUG_ENABLED + ":false}")
    private Boolean debugEnabled;

    @Override
    public String getInspectUrl() {
        return inspectUrl;
    }

    @Override
    public void setInspectUrl(String inspectUrl) {
        this.inspectUrl = inspectUrl;
    }

    @Override
    public String getInspectContextService() {
        return inspectContextService;
    }

    @Override
    public void setInspectContextService(String inspectContextService) {
        this.inspectContextService = inspectContextService;
    }

    @Override
    public int getSampleCount() {
        return sampleCount;
    }

    @Override
    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    @Override
    public String getResultFilter() {
        return resultFilter;
    }

    @Override
    public void setResultFilter(String resultFilter) {
        this.resultFilter = resultFilter;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}