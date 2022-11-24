package com.nepxion.discovery.automation.inspector.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.inspector.constant.InspectorTestConstant;
import com.nepxion.discovery.common.entity.ConditionStrategy;
import com.nepxion.discovery.common.util.YamlUtil;

public class InspectorTestCaseCondition extends ConditionStrategy {
    private static final long serialVersionUID = -7987731761366380912L;

    public static InspectorTestCaseCondition fromFile() {
        String input = getFile();

        return fromText(input);
    }

    public static String getFile() {
        return getFile(InspectorTestConstant.FILE_PATH_INSPECTOR);
    }

    public static InspectorTestCaseCondition fromFile(String file) {
        String input = getFile(file);

        return fromText(input);
    }

    public static InspectorTestCaseCondition fromText(String input) {
        return YamlUtil.fromYaml(input, InspectorTestCaseCondition.class);
    }

    public static String getFile(String file) {
        return TestUtil.getContent(file);
    }
}