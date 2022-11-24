package com.nepxion.discovery.automation.simulator.application.configuration;

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

import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConditionRoute;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseProperty;

@Configuration
public class SimulatorAutoConfiguration {
    @Bean
    public SimulatorTestCaseProperty simulatorTestCaseProperty() {
        return new SimulatorTestCaseProperty();
    }

    @Bean
    public SimulatorTestCaseCondition simulatorBasicTestCaseCondition() {
        return SimulatorTestCaseCondition.fromBasicFile();
    }

    @Bean
    public SimulatorTestCaseCondition simulatorReleaseTestCaseCondition() {
        return SimulatorTestCaseCondition.fromReleaseFile();
    }

    @Bean
    public SimulatorTestCaseConditionRoute simulatorReleaseTestCaseConditionRoute() {
        return SimulatorTestCaseConditionRoute.fromReleaseFile();
    }
}