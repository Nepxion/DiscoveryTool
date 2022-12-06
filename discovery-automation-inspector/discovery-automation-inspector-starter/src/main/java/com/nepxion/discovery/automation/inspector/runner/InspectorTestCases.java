package com.nepxion.discovery.automation.inspector.runner;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.nepxion.discovery.automation.common.runner.TestCases;
import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseEntity;
import com.nepxion.discovery.automation.inspector.strategy.InspectorTestStrategy;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.util.PluginInfoUtil;

public class InspectorTestCases extends TestCases {
    private static final Logger LOG = LoggerFactory.getLogger(InspectorTestCases.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    // 测试侦测场景
    public void testInspection(InspectorTestStrategy testStrategy) throws Exception {
        InspectorTestCaseEntity testCaseEntity = testStrategy.getTestCaseEntity();
        int sampleCount = testCaseEntity.getSampleCount();
        String inspectUrl = testCaseEntity.getInspectUrl();
        String resultFilter = testCaseEntity.getResultFilter();
        boolean debugEnabled = testCaseEntity.isDebugEnabled();

        LOG.info("侦测次数 : {}", sampleCount);

        InspectorEntity inspectorEntity = testStrategy.createInspectorEntity();

        Map<String, String> headerMap = testStrategy.getHeaderMap();

        HttpHeaders headers = new HttpHeaders();
        if (MapUtils.isNotEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                String headerName = entry.getKey();
                String headerValue = entry.getValue();
                if (StringUtils.isNotEmpty(headerName) && StringUtils.isNotEmpty(headerValue)) {
                    headers.add(headerName, headerValue);
                }
            }
        }

        HttpEntity<InspectorEntity> requestEntity = new HttpEntity<InspectorEntity>(inspectorEntity, headers);
        try {
            for (int i = 0; i < sampleCount; i++) {
                String result = testRestTemplate.postForEntity(inspectUrl, requestEntity, InspectorEntity.class).getBody().getResult();

                TestUtil.determineRestException(testRestTemplate);

                if (debugEnabled) {
                    LOG.info("详细侦测结果 : {}", result);
                }
                LOG.info("侦测结果 : {}", PluginInfoUtil.extractAll(result, resultFilter));
            }
        } catch (Exception e) {
            LOG.error("侦测失败", e);

            throw e;
        }
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }
}