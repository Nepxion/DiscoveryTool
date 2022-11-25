package com.nepxion.discovery.automation.simulator.data;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Arrays;
import java.util.List;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.util.StringUtil;

public class SimulatorTestCaseConditionData {
    private String expression;
    private List<String> parameter;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        this.parameter = extractParameter(expression);
    }

    public List<String> getParameter() {
        return parameter;
    }

    private List<String> extractParameter(String expression) {
        List<String> list = StringUtil.splitToList(expression, DiscoveryConstant.EQUALS + DiscoveryConstant.EQUALS);
        String parameter = list.get(0);
        String value = list.get(1);

        parameter = parameter.substring(parameter.indexOf("'") + 1, parameter.lastIndexOf("'")).trim();
        value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'")).trim();

        return Arrays.asList(parameter, value);
    }
}