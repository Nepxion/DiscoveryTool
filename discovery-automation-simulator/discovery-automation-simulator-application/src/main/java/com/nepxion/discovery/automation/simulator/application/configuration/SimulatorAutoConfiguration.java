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

import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseProperty;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseBasicCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseFirstCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseSecondCondition;

@Configuration
public class SimulatorAutoConfiguration {
    @Bean
    public SimulatorTestCaseProperty simulatorTestCaseProperty() {
        return new SimulatorTestCaseProperty();
    }

    @Bean
    public SimulatorTestCaseReleaseBasicCondition simulatorTestCaseReleaseBasicCondition() {
        return SimulatorTestCaseReleaseBasicCondition.fromFile();
    }

    @Bean
    public SimulatorTestCaseReleaseFirstCondition simulatorTestCaseReleaseFirstCondition() {
        return SimulatorTestCaseReleaseFirstCondition.fromFile();
    }

    @Bean
    public SimulatorTestCaseReleaseSecondCondition simulatorTestCaseReleaseSecondCondition() {
        return SimulatorTestCaseReleaseSecondCondition.fromFile();
    }
}