package com.nepxion.discovery.automation.inspector.console.configuration;

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
import com.nepxion.discovery.automation.inspector.console.endpoint.InspectorConsoleEndpoint;
import com.nepxion.discovery.automation.inspector.console.resource.InspectorConsoleResource;
import com.nepxion.discovery.automation.inspector.console.resource.InspectorConsoleResourceImpl;

@Configuration
@Import({ InspectorConsoleSwaggerConfiguration.class, ConsoleCorsRegistryConfiguration.class })
public class InspectorConsoleAutoConfiguration {
    protected static class ConsoleEndpointConfiguration {
        @Bean
        public InspectorConsoleResource inspectorConsoleResource() {
            return new InspectorConsoleResourceImpl();
        }

        @Bean
        public InspectorConsoleEndpoint inspectorConsoleEndpoint() {
            return new InspectorConsoleEndpoint();
        }
    }
}