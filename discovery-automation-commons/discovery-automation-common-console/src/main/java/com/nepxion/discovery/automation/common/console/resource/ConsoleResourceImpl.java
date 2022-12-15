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
import com.nepxion.discovery.automation.common.constant.TestConstant;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.common.util.UuidUtil;

public abstract class ConsoleResourceImpl implements ConsoleResource {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleResourceImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Value("${" + ConsoleConstant.SPRING_APPLICATION_LOGGER_MDC_KEY_SHOWN + ":true}")
    private Boolean loggerMdcKeyShown;

    @PostConstruct
    public void initialize() {
        String testThreadNamePrefix = getTestName() + "-";

        taskExecutor.setThreadNamePrefix(testThreadNamePrefix);
    }

    @Override
    public String test(String testConfig, boolean testCaseConfigWithYaml) {
        List<String> testConfigList = StringUtil.splitToList(testConfig, TestConstant.LINE_SEPARATE);

        int testConfigPartsCount = getTestConfigPartsCount();

        if (CollectionUtils.isEmpty(testConfigList) || testConfigList.size() != testConfigPartsCount) {
            throw new DiscoveryException("Test config must consists of " + testConfigPartsCount + " parts");
        }

        String uuid = UuidUtil.getTimeUUID();

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MDC.put(TestConstant.TESTCASE_ID, (loggerMdcKeyShown ? TestConstant.TESTCASE_ID + "=" : StringUtils.EMPTY) + uuid);

                String testName = getTestName();

                try {
                    beforeTest(testConfigList, testCaseConfigWithYaml);
                } catch (Exception e) {
                    LOG.error("[{}] execute BeforeTest failed", testName, e);

                    return;
                }

                try {
                    runTest(testConfigList, testCaseConfigWithYaml);
                } catch (InterruptedException e) {
                    // Ignored
                } catch (Exception e) {
                    LOG.error("[{}] execute RunTest failed", testName, e);
                } finally {
                    MDC.remove(TestConstant.TESTCASE_ID);

                    try {
                        afterTest(testConfigList, testCaseConfigWithYaml);
                    } catch (Exception e) {
                        LOG.error("[{}] execute AfterTest failed", testName, e);
                    }
                }
            }
        });

        return uuid;
    }

    public abstract String getTestName();

    public abstract int getTestConfigPartsCount();

    public abstract void beforeTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception;

    public abstract void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception;

    public abstract void afterTest(List<String> testConfigList, boolean testCaseConfigWithYaml) throws Exception;
}