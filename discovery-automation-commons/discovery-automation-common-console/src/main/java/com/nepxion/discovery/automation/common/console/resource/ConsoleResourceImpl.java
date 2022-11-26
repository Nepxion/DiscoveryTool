package com.nepxion.discovery.automation.common.console.resource;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nepxion.discovery.automation.common.console.constant.ConsoleConstant;
import com.nepxion.discovery.automation.common.console.entity.ConsoleThreadPoolProperties;
import com.nepxion.discovery.automation.common.constant.TestConstant;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.common.util.UuidUtil;

public abstract class ConsoleResourceImpl implements ConsoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleResourceImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ConsoleThreadPoolProperties consoleThreadPoolProperties;

    @Value("${" + ConsoleConstant.CONSOLE_AUTOMATION_LOGGER_MDC_KEY_SHOWN + ":true}")
    private Boolean loggerMdcKeyShown;

    @PostConstruct
    private void initialize() {
        String testThreadNamePrefix = getTestName() + "-Thread-";

        taskExecutor.setThreadNamePrefix(testThreadNamePrefix);

        int corePoolSize = consoleThreadPoolProperties.getCorePoolSize();
        int maxPoolSize = consoleThreadPoolProperties.getMaxPoolSize();
        int queueCapacity = consoleThreadPoolProperties.getQueueCapacity();
        int keepAliveSeconds = consoleThreadPoolProperties.getKeepAliveSeconds();
        int awaitTerminationSeconds = consoleThreadPoolProperties.getAwaitTerminationSeconds();

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
    }

    @Override
    public String test(String testConfig, boolean testCaseConfigWithYaml) {
        List<String> testConfigList = StringUtil.splitToList(testConfig, TestConstant.LINE_SEPARATE);

        int testConfigPartsCount = getTestConfigPartsCount();

        if (CollectionUtils.isEmpty(testConfigList) || testConfigList.size() != testConfigPartsCount) {
            throw new DiscoveryException("Test config must consists of " + testConfigPartsCount + " parts");
        }

        validateTest(testConfigList, testCaseConfigWithYaml);

        String uuid = UuidUtil.getTimeUUID();

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MDC.put(TestConstant.TESTCASE_ID, (loggerMdcKeyShown ? TestConstant.TESTCASE_ID + "=" : StringUtils.EMPTY) + uuid);

                String testName = getTestName() + " test";

                try {
                    runTest(testConfigList, testCaseConfigWithYaml);
                } catch (Exception e) {
                    LOG.error("{} failed", testName, e);

                    e.printStackTrace();
                } finally {
                    MDC.remove(TestConstant.TESTCASE_ID);

                    try {
                        finishTest(testConfigList, testCaseConfigWithYaml);
                    } catch (Exception e) {
                        LOG.error("{} failed", testName, e);

                        e.printStackTrace();
                    }
                }
            }
        });

        return uuid;
    }

    public abstract String getTestName();

    public abstract int getTestConfigPartsCount();

    public abstract void validateTest(List<String> testConfigList, boolean testCaseConfigWithYaml);

    public abstract void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception;

    public abstract void finishTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception;
}