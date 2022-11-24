package com.nepxion.discovery.automation.simulator.configuration;

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

import com.nepxion.discovery.automation.simulator.runner.SimulatorTestCases;
import com.nepxion.discovery.automation.simulator.runner.SimulatorTestRunner;

@Configuration
public class SimulatorTestAutoConfiguration {
    @Bean
    public SimulatorTestCases simulatorTestCases() {
        return new SimulatorTestCases();
    }

    @Bean
    public SimulatorTestRunner simulatorTestRunner() {
        return new SimulatorTestRunner();
    }
}