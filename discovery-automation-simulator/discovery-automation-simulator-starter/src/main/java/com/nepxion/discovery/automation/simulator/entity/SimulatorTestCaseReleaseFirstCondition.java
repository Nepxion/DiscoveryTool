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

public class SimulatorTestCaseReleaseFirstCondition extends ConditionStrategy {
    private static final long serialVersionUID = 4347083629171827044L;

    public static SimulatorTestCaseReleaseFirstCondition fromFile() {
        String input = getFile();

        return fromText(input);
    }

    public static String getFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_1);
    }

    public static SimulatorTestCaseReleaseFirstCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static SimulatorTestCaseReleaseFirstCondition fromText(String input) {
        return YamlUtil.fromYaml(input, SimulatorTestCaseReleaseFirstCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}