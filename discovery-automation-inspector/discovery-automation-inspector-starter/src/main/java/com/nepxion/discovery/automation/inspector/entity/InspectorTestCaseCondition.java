package com.nepxion.discovery.automation.inspector.entity;

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
import com.nepxion.discovery.automation.inspector.constant.InspectorTestConstant;
import com.nepxion.discovery.common.entity.ConditionStrategy;
import com.nepxion.discovery.common.util.YamlUtil;
import com.nepxion.discovery.common.yaml.YamlSafeConstructor;

public class InspectorTestCaseCondition extends ConditionStrategy {
    private static final long serialVersionUID = -7987731761366380912L;

    private static YamlSafeConstructor yamlSafeConstructor = new YamlSafeConstructor(new LinkedHashSet<Class<?>>(Arrays.asList(InspectorTestCaseCondition.class)));

    public static InspectorTestCaseCondition fromFile() {
        String input = getFile();

        return fromText(input);
    }

    public static String getFile() {
        return getFile(System.getProperty(InspectorTestConstant.TESTCASE_CONFIG_LOCATION, StringUtils.EMPTY) + InspectorTestConstant.FILE_PATH_INSPECTOR);
    }

    public static InspectorTestCaseCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static InspectorTestCaseCondition fromText(String input) {
        return YamlUtil.fromYaml(yamlSafeConstructor, input, InspectorTestCaseCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}