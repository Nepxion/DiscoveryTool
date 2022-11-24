package com.nepxion.discovery.automation.simulator.console.configuration;

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
import org.springframework.context.annotation.Import;

import com.nepxion.discovery.automation.common.console.configuration.ConsoleCorsRegistryConfiguration;
import com.nepxion.discovery.automation.simulator.console.endpoint.SimulatorConsoleEndpoint;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResource;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResourceImpl;

@Configuration
@Import({ SimulatorConsoleSwaggerConfiguration.class, ConsoleCorsRegistryConfiguration.class })
public class SimulatorConsoleAutoConfiguration {
    protected static class ConsoleEndpointConfiguration {
        @Bean
        public SimulatorConsoleResource simulatorConsoleResource() {
            return new SimulatorConsoleResourceImpl();
        }

        @Bean
        public SimulatorConsoleEndpoint simulatorConsoleEndpoint() {
            return new SimulatorConsoleEndpoint();
        }
    }
}