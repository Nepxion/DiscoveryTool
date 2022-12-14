package com.nepxion.discovery.automation.inspector.entity;

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
import com.nepxion.discovery.automation.inspector.constant.InspectorTestConstant;
import com.nepxion.discovery.common.util.ConfigurationUtil;

public class InspectorTestCaseConfig extends TestCaseConfig implements InspectorTestCaseEntity {
    private static final long serialVersionUID = -8654886373593752202L;

    private int sampleCount = 10;
    private String resultFilter = InspectorTestConstant.ID + "," + InspectorTestConstant.V;

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

    public static InspectorTestCaseConfig fromText(String input, boolean testCaseConfigWithYaml) throws Exception {
        return testCaseConfigWithYaml ? fromYamlText(input) : fromPropertiesText(input);
    }

    public static InspectorTestCaseConfig fromYamlText(String input) throws Exception {
        Map<String, String> map = null;
        try {
            map = ConfigurationUtil.parseYaml(input);
        } catch (Exception e) {
            throw e;
        }

        return fromMap(map);
    }

    public static InspectorTestCaseConfig fromPropertiesText(String input) throws Exception {
        Map<String, String> map = null;
        try {
            map = ConfigurationUtil.parseProperties(input);
        } catch (Exception e) {
            throw e;
        }

        return fromMap(map);
    }

    public static InspectorTestCaseConfig fromMap(Map<String, String> map) {
        InspectorTestCaseConfig testCaseConfig = new InspectorTestCaseConfig();

        String inspectUrl = map.get(InspectorTestConstant.TESTCASE_INSPECT_URL);
        if (StringUtils.isNotBlank(inspectUrl)) {
            testCaseConfig.setInspectUrl(inspectUrl);
        }

        String inspectContextService = map.get(InspectorTestConstant.TESTCASE_INSPECT_CONTEXT_SERVICE);
        if (StringUtils.isNotBlank(inspectContextService)) {
            testCaseConfig.setInspectContextService(inspectContextService);
        }

        String sampleCount = map.get(InspectorTestConstant.TESTCASE_SAMPLE_COUNT);
        if (StringUtils.isNotBlank(sampleCount)) {
            testCaseConfig.setSampleCount(Integer.valueOf(sampleCount));
        }

        String resultFilter = map.get(InspectorTestConstant.TESTCASE_RESULT_FILTER);
        if (StringUtils.isNotBlank(resultFilter)) {
            testCaseConfig.setResultFilter(resultFilter);
        }

        String debugEnabled = map.get(InspectorTestConstant.TESTCASE_DEBUG_ENABLED);
        if (StringUtils.isNotBlank(debugEnabled)) {
            testCaseConfig.setDebugEnabled(Boolean.valueOf(debugEnabled));
        }

        return testCaseConfig;
    }
}