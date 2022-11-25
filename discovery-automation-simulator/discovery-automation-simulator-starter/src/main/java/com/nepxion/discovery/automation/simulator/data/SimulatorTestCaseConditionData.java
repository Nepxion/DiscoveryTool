package com.nepxion.discovery.automation.simulator.data;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.TypeComparator;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.expression.DiscoveryExpressionResolver;
import com.nepxion.discovery.common.expression.DiscoveryTypeComparator;

public class SimulatorTestCaseConditionData {
    private static final TypeComparator TYPE_COMPARATOR = new DiscoveryTypeComparator();

    private String expression;
    private Map<String, String> parameter;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        if (StringUtils.isNotEmpty(expression)) {
            this.parameter = DiscoveryExpressionResolver.extractMap(expression);
        }
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public boolean isTriggered(Map<String, String> parameter) {
        return DiscoveryExpressionResolver.eval(expression, DiscoveryConstant.EXPRESSION_PREFIX, parameter, TYPE_COMPARATOR);
    }
}