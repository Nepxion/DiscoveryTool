package com.nepxion.discovery.automation.common.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TestCaseConfig implements TestCaseEntity, Serializable {
    private static final long serialVersionUID = 192985263228204044L;

    private String inspectUrl;
    private String inspectContextService = StringUtils.EMPTY;
    private boolean debugEnabled = false;

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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}