package com.nepxion.discovery.automation.simulator.constant;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.constant.TestConstant;

public class SimulatorTestConstant extends TestConstant {
    public static final String CONTEXT_PATH_SERVICE_INSTANCE_LIST = "service/instance-list/";
    public static final String CONTEXT_PATH_STRATEGY_CREATE_VERSION_RELEASE_YAML = "strategy/create-version-release-yaml/";
    public static final String CONTEXT_PATH_STRATEGY_RECREATE_VERSION_RELEASE_YAML = "strategy/recreate-version-release-yaml/";
    public static final String CONTEXT_PATH_STRATEGY_RESET_RELEASE = "strategy/reset-release/";

    public static final String FILE_PATH_VERSION_RELEASE_BASIC = "mock-version-release-basic.yaml";
    public static final String FILE_PATH_VERSION_RELEASE_1 = "mock-version-release-1.yaml";
    public static final String FILE_PATH_VERSION_RELEASE_2 = "mock-version-release-2.yaml";

    public static final String CONSOLE_URL = "console.url";
    public static final String CONSOLE_OPERATION_AWAIT_TIME = "console.operation.await.time";

    public static final String TESTCASE_GROUP = "testcase.group";
    public static final String TESTCASE_SERVICE = "testcase.service";

    public static final String TESTCASE_LOOP_COUNT = "testcase.loop.count";
    public static final String TESTCASE_BLUEGREEN_SAMPLE_COUNT = "testcase.bluegreen.sample.count";
    public static final String TESTCASE_GRAY_SAMPLE_COUNT = "testcase.gray.sample.count";
    public static final String TESTCASE_GRAY_WEIGHT_OFFSET = "testcase.gray.weight.offset";
    public static final String TESTCASE_VERSION_PREFER_ENABLED = "testcase.version.prefer.enabled";
    public static final String TESTCASE_SECOND_RELEASE_ENABLED = "testcase.second.release.enabled";
}