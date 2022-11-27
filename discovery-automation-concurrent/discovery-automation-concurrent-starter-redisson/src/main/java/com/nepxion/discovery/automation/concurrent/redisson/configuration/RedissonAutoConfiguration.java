package com.nepxion.discovery.automation.concurrent.redisson.configuration;

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

import com.nepxion.discovery.automation.concurrent.redisson.entity.RedissonProperties;
import com.nepxion.discovery.automation.concurrent.redisson.processor.RedissonLockProcessor;

@Configuration
@EnableConfigurationProperties({ RedissonProperties.class })
public class RedissonAutoConfiguration {
    @Bean
    public RedissonLockProcessor redissonLockProcessor() {
        return new RedissonLockProcessor();
    }
}