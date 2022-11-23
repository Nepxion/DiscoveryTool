package com.nepxion.discovery.automation.inspector.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public interface InspectorTestCaseEntity {
    String getInspectUrl();

    void setInspectUrl(String inspectUrl);

    String getInspectContextService();

    void setInspectContextService(String inspectContextService);

    int getSampleCount();

    void setSampleCount(int sampleCount);

    String getResultFilter();

    void setResultFilter(String resultFilter);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean debugEnabled);
}