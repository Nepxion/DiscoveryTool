package com.nepxion.discovery.automation.inspector.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.number.OrderingComparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.discovery.automation.common.logger.TestAssertLogger;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseCondition;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseConfig;
import com.nepxion.discovery.automation.inspector.entity.InspectorTestCaseEntity;
import com.nepxion.discovery.common.entity.InspectorEntity;

public class InspectorTestStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(InspectorTestStrategy.class);

    private String testCaseEntityContent;
    private String testCaseConditionContent;
    private boolean testCaseConfigWithYaml;

    private InspectorTestCaseEntity testCaseEntity;
    private InspectorTestCaseCondition testCaseCondition;

    private List<String> serviceIdList;

    private Map<String, String> headerMap;

    public void testInitialization(String testCaseEntityContent, String testCaseConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        this.testCaseEntityContent = testCaseEntityContent;
        this.testCaseConditionContent = testCaseConditionContent;
        this.testCaseConfigWithYaml = testCaseConfigWithYaml;

        testInitialization(InspectorTestCaseConfig.fromText(testCaseEntityContent, testCaseConfigWithYaml), InspectorTestCaseCondition.fromText(testCaseConditionContent));
    }

    public void testInitialization(InspectorTestCaseEntity testCaseEntity, InspectorTestCaseCondition testCaseCondition) throws Exception {
        this.testCaseEntity = testCaseEntity;
        this.testCaseCondition = testCaseCondition;

        LOG.info("侦测入口地址 : {}", testCaseEntity.getInspectUrl());
        LOG.info("侦测入口转发服务 : {}", testCaseEntity.getInspectContextService());

        LOG.info("采样总数 : {}", testCaseEntity.getSampleCount());

        LOG.info("结果过滤 : {}", testCaseEntity.getResultFilter());

        LOG.info("开启调试模式 : {}", testCaseEntity.isDebugEnabled());

        LOG.info("初始化服务列表...");
        initializeServiceIdList();
        LOG.info("服务列表 : {}", serviceIdList);

        LOG.info("初始化参数列表..");
        initializeHeaderMap();
        LOG.info("参数列表 : {}", headerMap);
    }

    private void initializeServiceIdList() throws Exception {
        serviceIdList = testCaseCondition.getService();
        TestAssertLogger.assertThat(LOG, "初始化失败，服务列表为空", CollectionUtils.isNotEmpty(serviceIdList) ? serviceIdList.size() : 0, OrderingComparison.greaterThanOrEqualTo(1));
    }

    private void initializeHeaderMap() throws Exception {
        headerMap = testCaseCondition.getHeader();
    }

    public String getTestCaseEntityContent() {
        return testCaseEntityContent;
    }

    public String getTestCaseConditionContent() {
        return testCaseConditionContent;
    }

    public boolean isTestCaseConfigWithYaml() {
        return testCaseConfigWithYaml;
    }

    public InspectorTestCaseEntity getTestCaseEntity() {
        return testCaseEntity;
    }

    public InspectorTestCaseCondition getTestCaseCondition() {
        return testCaseCondition;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public InspectorEntity createInspectorEntity() {
        String inspectContextService = testCaseEntity.getInspectContextService();

        InspectorEntity inspectorEntity = new InspectorEntity();
        inspectorEntity.setServiceIdList(serviceIdList);

        List<String> service = new ArrayList<String>(serviceIdList);
        if (StringUtils.isNotEmpty(inspectContextService) && service.contains(inspectContextService)) {
            service.remove(inspectContextService);
        }
        inspectorEntity.setServiceIdList(service);

        return inspectorEntity;
    }
}