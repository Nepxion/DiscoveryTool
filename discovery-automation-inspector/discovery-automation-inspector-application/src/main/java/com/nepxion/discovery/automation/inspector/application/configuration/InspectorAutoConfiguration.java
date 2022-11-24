package com.nepxion.discovery.automation.inspector.application.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseProperty;

@Configuration
public class InspectorAutoConfiguration {
    @Bean
    public InspectorTestCaseProperty inspectorTestCaseProperty() {
        return new InspectorTestCaseProperty();
    }

    @Bean
    public InspectorTestCaseCondition inspectorTestCaseCondition() {
        return InspectorTestCaseCondition.fromInspectorFile();
    }
}