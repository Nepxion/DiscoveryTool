package com.nepxion.discovery.automation.simulator.console.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleCaffeineLockProperties;
import com.nepxion.discovery.automation.common.console.entity.ConsoleRedissonLockProperties;
import com.nepxion.discovery.automation.simulator.console.endpoint.SimulatorConsoleEndpoint;
import com.nepxion.discovery.automation.simulator.console.lock.SimulatorConsoleCaffeineLock;
import com.nepxion.discovery.automation.simulator.console.lock.SimulatorConsoleLock;
import com.nepxion.discovery.automation.simulator.console.lock.SimulatorConsoleRedissonLock;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResource;
import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResourceImpl;

@Configuration
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

    @ConditionalOnProperty(value = ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE, havingValue = ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE_CAFFEINE, matchIfMissing = true)
    @EnableConfigurationProperties({ ConsoleCaffeineLockProperties.class })
    protected static class ConsoleCaffeineLockConfiguration {
        @Bean
        public SimulatorConsoleLock simulatorConsoleLock() {
            return new SimulatorConsoleCaffeineLock();
        }
    }

    @ConditionalOnProperty(value = ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE, havingValue = ConsoleConstant.CONSOLE_AUTOMATION_LOCK_TYPE_REDISSON, matchIfMissing = false)
    @EnableConfigurationProperties({ ConsoleRedissonLockProperties.class })
    protected static class ConsoleRedissonLockConfiguration {
        @Bean
        public SimulatorConsoleLock simulatorConsoleLock() {
            return new SimulatorConsoleRedissonLock();
        }
    }
}