package com.nepxion.discovery.automation.simulator.console.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.nepxion.discovery.automation.common.console.entity.ConsoleCachePoolProperties;
import com.nepxion.discovery.automation.simulator.console.concurrent.SimulatorConsoleCaffeineConcurrent;
import com.nepxion.discovery.automation.simulator.console.concurrent.SimulatorConsoleConcurrent;
import com.nepxion.discovery.automation.simulator.console.endpoint.SimulatorConsoleEndpoint;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResource;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResourceImpl;

@Configuration
@EnableConfigurationProperties({ ConsoleCachePoolProperties.class })
@Import({ SimulatorConsoleSwaggerConfiguration.class })
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
    
    @Bean
    public SimulatorConsoleConcurrent simulatorConsoleConcurrent() {
        return new SimulatorConsoleCaffeineConcurrent();
    }
}