package com.nepxion.discovery.automation.common.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public interface TestCaseEntity {
    String getInspectUrl();

    void setInspectUrl(String inspectUrl);

    String getInspectContextService();

    void setInspectContextService(String inspectContextService);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean debugEnabled);
}