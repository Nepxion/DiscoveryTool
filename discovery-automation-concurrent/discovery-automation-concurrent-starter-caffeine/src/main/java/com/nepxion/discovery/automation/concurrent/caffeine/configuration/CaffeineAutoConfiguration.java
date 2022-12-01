package com.nepxion.discovery.automation.concurrent.caffeine.configuration;

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

import com.nepxion.discovery.automation.concurrent.caffeine.entity.CaffeineProperties;
import com.nepxion.discovery.automation.concurrent.caffeine.lock.CaffeineLockWrapper;

@Configuration
@EnableConfigurationProperties({ CaffeineProperties.class })
public class CaffeineAutoConfiguration {
    @Bean
    public CaffeineLockWrapper caffeineLockWrapper() {
        return new CaffeineLockWrapper();
    }
}