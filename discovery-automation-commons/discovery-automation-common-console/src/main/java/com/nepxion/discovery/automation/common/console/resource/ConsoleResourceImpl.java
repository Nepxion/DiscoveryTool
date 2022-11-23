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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nepxion.discovery.automation.common.constant.TestConstant;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.util.StringUtil;

public abstract class ConsoleResourceImpl implements ConsoleResource {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    private void initialize() {
        String testThreadNamePrefix = getTestThreadNamePrefix();

        taskExecutor.setThreadNamePrefix(testThreadNamePrefix);
    }

    @Override
    public void test(String testConfig, boolean testCaseConfigWithYaml) {
        List<String> testConfigList = StringUtil.splitToList(testConfig, TestConstant.LINE_SEPARATE);

        int testConfigPartsCount = getTestConfigPartsCount();

        if (CollectionUtils.isEmpty(testConfigList) || testConfigList.size() != testConfigPartsCount) {
            throw new DiscoveryException("Test config must consists of " + testConfigPartsCount + " parts");
        }

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                runTest(testConfigList, testCaseConfigWithYaml);
            }
        });
    }

    public abstract String getTestThreadNamePrefix();

    public abstract int getTestConfigPartsCount();

    public abstract void runTest(List<String> testConfigList, boolean testCaseConfigWithYaml);
}