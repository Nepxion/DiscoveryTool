package com.nepxion.discovery.automation.common.console.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.nepxion.discovery.automation.common.console.entity.ConsoleThreadPoolProperties;

@Configuration
@EnableConfigurationProperties({ ConsoleThreadPoolProperties.class })
@Import({ ConsoleCorsRegistryConfiguration.class })
public class ConsoleAutoConfiguration {

}