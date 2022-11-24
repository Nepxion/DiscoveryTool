package com.nepxion.discovery.automation.simulator.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.entity.TestCaseEntity;

public interface SimulatorTestCaseEntity extends TestCaseEntity {
    String getConsoleUrl();

    void setConsoleUrl(String consoleUrl);

    int getConsoleOperationAwaitTime();

    void setConsoleOperationAwaitTime(int consoleOperationAwaitTime);

    String getGroup();

    void setGroup(String group);

    String getServiceId();

    void setServiceId(String serviceId);

    int getLoopCount();

    void setLoopCount(int loopCount);

    int getBlueGreenSampleCount();

    void setBlueGreenSampleCount(int blueGreenSampleCount);

    int getGraySampleCount();

    void setGraySampleCount(int graySampleCount);

    int getGrayWeightOffset();

    void setGrayWeightOffset(int grayWeightOffset);

    boolean isVersionPreferEnabled();

    void setVersionPreferEnabled(boolean versionPreferEnabled);

    boolean isSecondReleaseEnabled();

    void setSecondReleaseEnabled(boolean secondReleaseEnabled);
}