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

public class SimulatorTestCaseReleaseBasicCondition extends ConditionStrategy {
    private static final long serialVersionUID = -1234485339530269667L;

    public static SimulatorTestCaseReleaseBasicCondition fromFile() {
        String input = getFile();

        return fromText(input);
    }

    public static String getFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_BASIC);
    }

    public static SimulatorTestCaseReleaseBasicCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static SimulatorTestCaseReleaseBasicCondition fromText(String input) {
        return YamlUtil.fromYaml(input, SimulatorTestCaseReleaseBasicCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}