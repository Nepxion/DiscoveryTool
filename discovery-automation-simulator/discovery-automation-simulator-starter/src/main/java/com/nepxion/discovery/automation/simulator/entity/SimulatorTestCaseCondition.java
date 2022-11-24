package com.nepxion.discovery.automation.simulator.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.common.entity.ConditionStrategy;
import com.nepxion.discovery.common.util.YamlUtil;

public class SimulatorTestCaseCondition extends ConditionStrategy {
    private static final long serialVersionUID = -1234485339530269667L;

    public static SimulatorTestCaseCondition fromBasicFile() {
        String input = getBasicFile();

        return fromText(input);
    }

    public static String getBasicFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_BASIC);
    }

    public static SimulatorTestCaseCondition fromReleaseFile() {
        String input = getReleaseFile();

        return fromText(input);
    }

    public static String getReleaseFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_1);
    }

    public static SimulatorTestCaseCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static SimulatorTestCaseCondition fromText(String input) {
        return YamlUtil.fromYaml(input, SimulatorTestCaseCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}