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

import com.nepxion.discovery.automation.common.entity.TestCaseProperty;
import com.nepxion.discovery.automation.inspector.constant.InspectorTestConstant;

public class InspectorTestCaseProperty extends TestCaseProperty implements InspectorTestCaseEntity {
    @Value("${" + InspectorTestConstant.TESTCASE_SAMPLE_COUNT + ":10}")
    private Integer sampleCount;

    @Value("${" + InspectorTestConstant.TESTCASE_RESULT_FILTER + ":" + InspectorTestConstant.ID + "," + InspectorTestConstant.V + "}")
    private String resultFilter;

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
}