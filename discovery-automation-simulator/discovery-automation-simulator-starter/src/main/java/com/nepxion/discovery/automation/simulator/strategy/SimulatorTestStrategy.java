package com.nepxion.discovery.automation.simulator.strategy;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.number.OrderingComparison;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nepxion.discovery.automation.common.logger.TestAssertLogger;
import com.nepxion.discovery.automation.common.strategy.TestStrategy;
import com.nepxion.discovery.automation.common.util.TestUtil;
import com.nepxion.discovery.automation.simulator.constant.SimulatorTestConstant;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseConfig;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseEntity;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseBasicCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseFirstCondition;
import com.nepxion.discovery.automation.simulator.entity.SimulatorTestCaseReleaseSecondCondition;
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.ConditionBlueGreenEntity;
import com.nepxion.discovery.common.entity.ConditionBlueGreenRoute;
import com.nepxion.discovery.common.entity.ConditionGrayEntity;
import com.nepxion.discovery.common.entity.InspectorEntity;
import com.nepxion.discovery.common.entity.InstanceEntity;
import com.nepxion.discovery.common.entity.VersionSortType;
import com.nepxion.discovery.common.util.JsonUtil;
import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.common.util.VersionSortUtil;

public class SimulatorTestStrategy extends TestStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorTestStrategy.class);

    private TestRestTemplate testRestTemplate;

    private String testCaseEntityContent;
    private String testCaseReleaseBasicConditionContent;
    private String testCaseReleaseFirstConditionContent;
    private String testCaseReleaseSecondConditionContent;
    private boolean testCaseConfigWithYaml;

    private SimulatorTestCaseEntity testCaseEntity;
    private SimulatorTestCaseReleaseBasicCondition testCaseReleaseBasicCondition;
    private SimulatorTestCaseReleaseFirstCondition testCaseReleaseFirstCondition;
    private SimulatorTestCaseReleaseSecondCondition testCaseReleaseSecondCondition;

    private List<String> serviceIdList;

    private Map<String, List<String>> allVersionMap;
    private Map<String, List<String>> oldVersionMap;
    private Map<String, List<String>> newVersionMap;
    private List<String> allVersionList;
    private List<String> oldVersionList;
    private List<String> newVersionList;

    private String greenExpression;
    private List<String> greenParameter;
    private String blueExpression;
    private List<String> blueParameter;
    private String grayExpression0;
    private List<String> grayParameter0;
    private List<Integer> grayWeight0;
    private String grayExpression1;
    private List<String> grayParameter1;
    private List<Integer> grayWeight1;

    public void testInitialization(String testCaseEntityContent, String testCaseReleaseBasicConditionContent, String testCaseReleaseFirstConditionContent, String testCaseReleaseSecondConditionContent, boolean testCaseConfigWithYaml) throws Exception {
        this.testCaseEntityContent = testCaseEntityContent;
        this.testCaseReleaseBasicConditionContent = testCaseReleaseBasicConditionContent;
        this.testCaseReleaseFirstConditionContent = testCaseReleaseFirstConditionContent;
        this.testCaseReleaseSecondConditionContent = testCaseReleaseSecondConditionContent;
        this.testCaseConfigWithYaml = testCaseConfigWithYaml;

        testInitialization(SimulatorTestCaseConfig.fromText(testCaseEntityContent, testCaseConfigWithYaml), SimulatorTestCaseReleaseBasicCondition.fromText(testCaseReleaseBasicConditionContent), SimulatorTestCaseReleaseFirstCondition.fromText(testCaseReleaseFirstConditionContent), SimulatorTestCaseReleaseSecondCondition.fromText(testCaseReleaseSecondConditionContent));
    }

    public void testInitialization(SimulatorTestCaseEntity testCaseEntity, SimulatorTestCaseReleaseBasicCondition testCaseReleaseBasicCondition, SimulatorTestCaseReleaseFirstCondition testCaseReleaseFirstCondition, SimulatorTestCaseReleaseSecondCondition testCaseReleaseSecondCondition) throws Exception {
        this.testCaseEntity = testCaseEntity;
        this.testCaseReleaseBasicCondition = testCaseReleaseBasicCondition;
        this.testCaseReleaseFirstCondition = testCaseReleaseFirstCondition;
        this.testCaseReleaseSecondCondition = testCaseReleaseSecondCondition;

        LOG.info("控制台地址 : {}", testCaseEntity.getConsoleUrl());
        LOG.info("控制台等待生效时间 : {} 毫秒", testCaseEntity.getConsoleOperationAwaitTime());

        LOG.info("订阅的组名 : {}", testCaseEntity.getGroup());
        LOG.info("订阅的服务名 : {}", testCaseEntity.getServiceId());

        LOG.info("侦测入口地址 : {}", testCaseEntity.getInspectUrl());
        LOG.info("侦测入口转发服务 : {}", testCaseEntity.getInspectContextService());

        LOG.info("测试用例执行循环次数 : {}", testCaseEntity.getLoopCount());

        LOG.info("蓝绿发布采样总数 : {}", testCaseEntity.getBlueGreenSampleCount());
        LOG.info("灰度发布采样总数 : {}", testCaseEntity.getGraySampleCount());
        LOG.info("灰度权重准确率偏离值 : {}", testCaseEntity.getGrayWeightOffset());

        LOG.info("开启版本偏好部署模式 : {}", testCaseEntity.isVersionPreferEnabled());
        LOG.info("开启第二次蓝绿灰度发布自动化测试 : {}", testCaseEntity.isSecondReleaseEnabled());

        LOG.info("开启调试模式 : {}", testCaseEntity.isDebugEnabled());

        LOG.info("初始化服务列表...");
        initializeServiceIdList();
        LOG.info("服务列表 : {}", serviceIdList);

        LOG.info("初始化服务版本列表...");
        initializeVersionContext();
        LOG.info("服务版本排序依据 : {}", testCaseReleaseFirstCondition.getSort());
        LOG.info("服务全部版本列表 : {}", allVersionMap);
        LOG.info("服务旧版本列表 : {}", oldVersionMap);
        LOG.info("服务新版本列表 : {}", newVersionMap);

        LOG.info("初始化表达式参数列表...");
        initializeParameter();
        if (hasBlueGreen()) {
            LOG.info("绿表达式 : {}，参数 : {}", greenExpression, greenParameter);
            LOG.info("蓝表达式 : {}，参数 : {}", blueExpression, blueParameter);
        } else {
            LOG.info("蓝绿规则策略 : 未配置");
        }
        if (hasGray()) {
            LOG.info("灰度表达式1 : {}，参数 : {}，权重 : {}", grayExpression0, grayParameter0, grayWeight0);
            LOG.info("灰度表达式2 : {}，参数 : {}，权重 : {}", grayExpression1, grayParameter1, grayWeight1);
        } else {
          LOG.info("灰度规则策略 : 未配置");
        }
    }

    private void initializeServiceIdList() throws Exception {
        serviceIdList = testCaseReleaseFirstCondition.getService();
        TestAssertLogger.assertThat(LOG, "初始化失败，服务列表为空", CollectionUtils.isNotEmpty(serviceIdList) ? serviceIdList.size() : 0, OrderingComparison.greaterThanOrEqualTo(1));
    }

    private void initializeVersionContext() throws Exception {
        allVersionMap = new LinkedHashMap<String, List<String>>();
        oldVersionMap = new LinkedHashMap<String, List<String>>();
        newVersionMap = new LinkedHashMap<String, List<String>>();
        for (String serviceId : serviceIdList) {
            List<InstanceEntity> instanceList = retrieveInstanceList(serviceId);
            TestAssertLogger.assertThat(LOG, "初始化失败，服务【" + serviceId + "】的实例列表为空", CollectionUtils.isNotEmpty(instanceList) ? instanceList.size() : 0, OrderingComparison.greaterThanOrEqualTo(1));

            String sort = testCaseReleaseFirstCondition.getSort();
            VersionSortType versionSortType = VersionSortType.fromString(sort);

            List<String> versionList = VersionSortUtil.assembleVersionList(instanceList, versionSortType);
            TestAssertLogger.assertThat(LOG, "初始化失败，服务【" + serviceId + "】的版本个数至少2个", CollectionUtils.isNotEmpty(versionList) ? versionList.size() : 0, OrderingComparison.greaterThanOrEqualTo(2));

            allVersionMap.put(serviceId, versionList);

            List<String> oldVersionList = new ArrayList<String>();
            oldVersionList.add(versionList.get(0));
            oldVersionMap.put(serviceId, oldVersionList);

            List<String> newVersionList = new ArrayList<String>();
            if (versionList.size() == 1) {
                newVersionList.add(versionList.get(0));
            } else {
                newVersionList.addAll(versionList);
                newVersionList.remove(0);
            }
            newVersionMap.put(serviceId, newVersionList);
        }

        allVersionList = convertVesionList(allVersionMap);
        oldVersionList = convertVesionList(oldVersionMap);
        newVersionList = convertVesionList(newVersionMap);
    }

    private List<String> convertVesionList(Map<String, List<String>> versionMap) {
        List<String> versionList = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : versionMap.entrySet()) {
            String serviceId = entry.getKey();
            List<String> versions = entry.getValue();
            for (String version : versions) {
                versionList.add(serviceId + SimulatorTestConstant.SEPARATE + version);
            }
        }

        return versionList;
    }

    private void initializeParameter() {
        List<ConditionBlueGreenEntity> blueGreen = testCaseReleaseFirstCondition.getBlueGreen();
        if (CollectionUtils.isNotEmpty(blueGreen)) {
            for (ConditionBlueGreenEntity conditionBlueGreenEntity : blueGreen) {
                String blueGreenExpression = conditionBlueGreenEntity.getExpression();
                String bludGreenRoute = conditionBlueGreenEntity.getRoute();
                ConditionBlueGreenRoute conditionBlueGreenRoute = ConditionBlueGreenRoute.fromString(bludGreenRoute);
                switch (conditionBlueGreenRoute) {
                    case GREEN:
                        greenExpression = blueGreenExpression;
                        greenParameter = extractParameter(blueGreenExpression);
                        break;
                    case BLUE:
                        blueExpression = blueGreenExpression;
                        blueParameter = extractParameter(blueGreenExpression);
                        break;
                }
            }
        }

        List<ConditionGrayEntity> gray = testCaseReleaseFirstCondition.getGray();
        if (CollectionUtils.isNotEmpty(gray)) {
            ConditionGrayEntity conditionGrayEntity0 = gray.get(0);
            grayExpression0 = conditionGrayEntity0.getExpression();
            grayParameter0 = extractParameter(grayExpression0);
            grayWeight0 = conditionGrayEntity0.getWeight();

            ConditionGrayEntity conditionGrayEntity1 = gray.get(1);
            grayExpression1 = conditionGrayEntity1.getExpression();
            grayParameter1 = extractParameter(grayExpression1);
            grayWeight1 = conditionGrayEntity1.getWeight();
        }
    }

    private List<String> extractParameter(String expression) {
        List<String> list = StringUtil.splitToList(expression, DiscoveryConstant.EQUALS + DiscoveryConstant.EQUALS);
        String parameter = list.get(0);
        String value = list.get(1);

        parameter = parameter.substring(parameter.indexOf("'") + 1, parameter.lastIndexOf("'")).trim();
        value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'")).trim();

        return Arrays.asList(parameter, value);
    }

    public TestRestTemplate getTestRestTemplate() {
        return testRestTemplate;
    }

    public void setTestRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    public String getTestCaseEntityContent() {
        return testCaseEntityContent;
    }

    public String getTestCaseReleaseBasicConditionContent() {
        return testCaseReleaseBasicConditionContent;
    }

    public String getTestCaseReleaseFirstConditionContent() {
        return testCaseReleaseFirstConditionContent;
    }

    public String getTestCaseReleaseSecondConditionContent() {
        return testCaseReleaseSecondConditionContent;
    }

    public boolean isTestCaseConfigWithYaml() {
        return testCaseConfigWithYaml;
    }

    public SimulatorTestCaseEntity getTestCaseEntity() {
        return testCaseEntity;
    }

    public SimulatorTestCaseReleaseBasicCondition getTestCaseReleaseBasicCondition() {
        return testCaseReleaseBasicCondition;
    }

    public SimulatorTestCaseReleaseFirstCondition getTestCaseReleaseFirstCondition() {
        return testCaseReleaseFirstCondition;
    }

    public SimulatorTestCaseReleaseSecondCondition getTestCaseReleaseSecondCondition() {
        return testCaseReleaseSecondCondition;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public Map<String, List<String>> getAllVersionMap() {
        return allVersionMap;
    }

    public Map<String, List<String>> getOldVersionMap() {
        return oldVersionMap;
    }

    public Map<String, List<String>> getNewVersionMap() {
        return newVersionMap;
    }

    public List<String> getAllVersionList() {
        return allVersionList;
    }

    public List<String> getOldVersionList() {
        return oldVersionList;
    }

    public List<String> getNewVersionList() {
        return newVersionList;
    }

    public String getGreenExpression() {
        return greenExpression;
    }

    public List<String> getGreenParameter() {
        return greenParameter;
    }

    public String getBlueExpression() {
        return blueExpression;
    }

    public List<String> getBlueParameter() {
        return blueParameter;
    }

    public String getGrayExpression0() {
        return grayExpression0;
    }

    public List<String> getGrayParameter0() {
        return grayParameter0;
    }

    public List<Integer> getGrayWeight0() {
        return grayWeight0;
    }

    public String getGrayExpression1() {
        return grayExpression1;
    }

    public List<String> getGrayParameter1() {
        return grayParameter1;
    }

    public List<Integer> getGrayWeight1() {
        return grayWeight1;
    }

    public boolean hasBlueGreen() {
        return CollectionUtils.isNotEmpty(testCaseReleaseFirstCondition.getBlueGreen());
    }

    public boolean hasGray() {
        return CollectionUtils.isNotEmpty(testCaseReleaseFirstCondition.getGray());
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

    public List<InstanceEntity> retrieveInstanceList(String serviceId) {
        String consoleUrl = testCaseEntity.getConsoleUrl();

        try {
            String url = consoleUrl + TestUtil.formatContextPath(SimulatorTestConstant.CONTEXT_PATH_SERVICE_INSTANCE_LIST) + serviceId;
            String result = testRestTemplate.getForEntity(url, String.class).getBody();

            TestUtil.determineRestException(testRestTemplate);

            return JsonUtil.fromJson(result, new TypeReference<List<InstanceEntity>>() {
            });
        } catch (Exception e) {
            LOG.error("获取服务【" + serviceId + "】的实例列表失败", e);

            throw e;
        }
    }

    public void createVersionRelease(String input) throws Exception {
        String consoleUrl = testCaseEntity.getConsoleUrl();
        int consoleOperationAwaitTime = testCaseEntity.getConsoleOperationAwaitTime();
        String group = testCaseEntity.getGroup();
        String serviceId = testCaseEntity.getServiceId();

        LOG.info("输入规则策略 : {}", input != null ? "\n" + input : "无");

        String url = consoleUrl + TestUtil.formatContextPath(SimulatorTestConstant.CONTEXT_PATH_STRATEGY_CREATE_VERSION_RELEASE_YAML) + group + "/" + serviceId;
        String output = testRestTemplate.postForEntity(url, input, String.class).getBody();

        TestUtil.determineRestException(testRestTemplate);

        LOG.info("输出规则策略 : {}", output != null ? "\n" + output : "无");

        Thread.sleep(consoleOperationAwaitTime);
    }

    public void recreateVersionRelease(String input) throws Exception {
        String consoleUrl = testCaseEntity.getConsoleUrl();
        int consoleOperationAwaitTime = testCaseEntity.getConsoleOperationAwaitTime();
        String group = testCaseEntity.getGroup();
        String serviceId = testCaseEntity.getServiceId();

        LOG.info("输入规则策略 : {}", input != null ? "\n" + input : "无");

        String url = consoleUrl + TestUtil.formatContextPath(SimulatorTestConstant.CONTEXT_PATH_STRATEGY_RECREATE_VERSION_RELEASE_YAML) + group + "/" + serviceId;
        String output = testRestTemplate.postForEntity(url, input, String.class).getBody();

        TestUtil.determineRestException(testRestTemplate);

        LOG.info("输出规则策略 : {}", output != null ? "\n" + output : "无");

        Thread.sleep(consoleOperationAwaitTime);
    }

    public void resetRelease() throws Exception {
        String consoleUrl = testCaseEntity.getConsoleUrl();
        int consoleOperationAwaitTime = testCaseEntity.getConsoleOperationAwaitTime();
        String group = testCaseEntity.getGroup();
        String serviceId = testCaseEntity.getServiceId();

        LOG.info("输入规则策略 : 无");

        String url = consoleUrl + TestUtil.formatContextPath(SimulatorTestConstant.CONTEXT_PATH_STRATEGY_RESET_RELEASE) + group + "/" + serviceId;
        String output = testRestTemplate.postForEntity(url, null, String.class).getBody();

        TestUtil.determineRestException(testRestTemplate);

        LOG.info("输出规则策略 : {}", output != null ? "\n" + output : "无");

        Thread.sleep(consoleOperationAwaitTime);
    }
}