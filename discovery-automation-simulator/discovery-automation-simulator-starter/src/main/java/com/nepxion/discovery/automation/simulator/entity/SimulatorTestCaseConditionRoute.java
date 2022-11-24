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
import com.nepxion.discovery.common.entity.ConditionRouteStrategy;
import com.nepxion.discovery.common.util.YamlUtil;

public class SimulatorTestCaseConditionRoute extends ConditionRouteStrategy {
    private static final long serialVersionUID = 7160851936643268682L;

    public static SimulatorTestCaseConditionRoute fromBasicFile() {
        String input = getBasicFile();

        return fromText(input);
    }

    public static String getBasicFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_BASIC);
    }

    public static SimulatorTestCaseConditionRoute fromReleaseFile() {
        String input = getReleaseFile();

        return fromText(input);
    }

    public static String getReleaseFile() {
        return getFile(SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_2);
    }

    public static SimulatorTestCaseConditionRoute fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static SimulatorTestCaseConditionRoute fromText(String input) {
        return YamlUtil.fromYaml(input, SimulatorTestCaseConditionRoute.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}