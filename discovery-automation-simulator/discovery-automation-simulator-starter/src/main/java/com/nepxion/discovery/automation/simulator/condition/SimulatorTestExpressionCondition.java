package com.nepxion.discovery.automation.simulator.condition;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;

public class SimulatorTestExpressionCondition {
    private Pattern pattern = Pattern.compile(SimulatorTestConstant.EXPRESSION_REGEX);

    public Map<String, String> extractParameter(String expression) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        Matcher matcher = pattern.matcher(expression);
        int index = 0;
        String key = null;
        while (matcher.find()) {
            String group = matcher.group();
            if (group.startsWith(SimulatorTestConstant.SINGLE_QUOTES) && group.endsWith(SimulatorTestConstant.SINGLE_QUOTES)) {
                String name = StringUtils.substringBetween(group, SimulatorTestConstant.SINGLE_QUOTES, SimulatorTestConstant.SINGLE_QUOTES);
                if (index % 2 == 0) {
                    // 偶数个元素为Key
                    key = name;
                } else if (index % 2 == 1) {
                    // 奇数个元素为Value
                    map.put(key, name);
                }

                index++;
            }
        }

        return map;
    }
}