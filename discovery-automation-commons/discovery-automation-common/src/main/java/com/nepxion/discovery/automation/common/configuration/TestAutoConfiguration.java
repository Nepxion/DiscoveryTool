package com.nepxion.discovery.automation.common.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.discovery.common.handler.DiscoveryResponseErrorHandler;

@Configuration
public class TestAutoConfiguration {
    @Bean
    public TestRestTemplate testRestTemplate() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().setErrorHandler(new DiscoveryResponseErrorHandler());

        return testRestTemplate;
    }
}