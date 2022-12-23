package com.nepxion.discovery.automation.simulator.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Arrays;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.common.entity.ConditionRouteStrategy;
import com.nepxion.discovery.common.util.YamlUtil;
import com.nepxion.discovery.common.yaml.YamlSafeConstructor;

public class SimulatorTestCaseReleaseSecondCondition extends ConditionRouteStrategy {
    private static final long serialVersionUID = 7160851936643268682L;

    private static YamlSafeConstructor yamlSafeConstructor = new YamlSafeConstructor(new LinkedHashSet<Class<?>>(Arrays.asList(SimulatorTestCaseReleaseSecondCondition.class)));

    public static SimulatorTestCaseReleaseSecondCondition fromFile() {
        String input = getFile();

        return fromText(input);
    }

    public static String getFile() {
        return getFile(System.getProperty(SimulatorTestConstant.TESTCASE_CONFIG_LOCATION, StringUtils.EMPTY) + SimulatorTestConstant.FILE_PATH_VERSION_RELEASE_2);
    }

    public static SimulatorTestCaseReleaseSecondCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static SimulatorTestCaseReleaseSecondCondition fromText(String input) {
        return YamlUtil.fromYaml(yamlSafeConstructor, input, SimulatorTestCaseReleaseSecondCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}