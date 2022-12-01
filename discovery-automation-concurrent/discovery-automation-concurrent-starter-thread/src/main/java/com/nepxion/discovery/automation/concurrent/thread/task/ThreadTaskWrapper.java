package com.nepxion.discovery.automation.concurrent.thread.task;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nepxion.discovery.automation.concurrent.thread.entity.ThreadProperties;

public class ThreadTaskWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadTaskWrapper.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ThreadProperties threadProperties;

    @PostConstruct
    private void initialize() {
        LOG.info("Start to initialize ThreadPoolTaskExecutor...");

        int corePoolSize = threadProperties.getCorePoolSize();
        int maxPoolSize = threadProperties.getMaxPoolSize();
        int queueCapacity = threadProperties.getQueueCapacity();
        int keepAliveSeconds = threadProperties.getKeepAliveSeconds();
        int awaitTerminationSeconds = threadProperties.getAwaitTerminationSeconds();

        if (corePoolSize > 0) {
            taskExecutor.setCorePoolSize(corePoolSize);
        }
        if (maxPoolSize > 0) {
            taskExecutor.setMaxPoolSize(maxPoolSize);
        }
        if (queueCapacity > 0) {
            taskExecutor.setQueueCapacity(queueCapacity);
        }
        if (keepAliveSeconds > 0) {
            taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        }
        if (awaitTerminationSeconds > 0) {
            taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        }

        LOG.info("ThreadPoolTaskExecutor core pool size : {}", taskExecutor.getCorePoolSize());
        LOG.info("ThreadPoolTaskExecutor max pool size : {}", taskExecutor.getMaxPoolSize());
        LOG.info("ThreadPoolTaskExecutor keep alive seconds : {}", taskExecutor.getKeepAliveSeconds());
    }
}