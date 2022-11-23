package com.nepxion.discovery.automation.inspector.configuration;

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

import com.nepxion.discovery.automation.inspector.runner.InspectorTestCases;
import com.nepxion.discovery.automation.inspector.runner.InspectorTestRunner;

@Configuration
public class InspectorTestAutoConfiguration {
    @Bean
    public InspectorTestCases testCases() {
        return new InspectorTestCases();
    }

    @Bean
    public InspectorTestRunner testRunner() {
        return new InspectorTestRunner();
    }
}