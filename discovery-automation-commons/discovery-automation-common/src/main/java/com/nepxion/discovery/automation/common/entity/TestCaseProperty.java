package com.nepxion.discovery.automation.common.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Value;

import com.nepxion.discovery.automation.common.constant.TestConstant;

public class TestCaseProperty implements TestCaseEntity {
    @Value("${" + TestConstant.TESTCASE_INSPECT_URL + "}")
    private String inspectUrl;

    @Value("${" + TestConstant.TESTCASE_INSPECT_CONTEXT_SERVICE + ":}")
    private String inspectContextService;

    @Value("${" + TestConstant.TESTCASE_DEBUG_ENABLED + ":false}")
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
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}