package com.nepxion.discovery.automation.simulator.console.endpoint;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nepxion.discovery.automation.simulator.console.resource.SimulatorConsoleResource;
import com.nepxion.discovery.common.util.ResponseUtil;

@RestController
@RequestMapping(path = "/simulator-test")
@Api(tags = { "自动化模拟流程测试接口" })
public class SimulatorConsoleEndpoint {
    @Autowired
    private SimulatorConsoleResource simulatorConsoleResource;

    @RequestMapping(path = "/test-config-yaml", method = RequestMethod.POST)
    @ApiOperation(value = "全链路自动化模拟流程测试", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    public ResponseEntity<?> testConfigYaml(@RequestBody @ApiParam(value = "测试配置文本，按照次序，由application.yaml、version-release-basic.yaml、version-release-1.yaml、version-release-2.yaml组成，中间用10个\"-\"组成换行分隔", required = true) String testConfig) {
        return doTest(testConfig, true);
    }

    @RequestMapping(path = "/test-config-properties", method = RequestMethod.POST)
    @ApiOperation(value = "全链路自动化模拟流程测试", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    public ResponseEntity<?> testConfigProperties(@RequestBody @ApiParam(value = "测试配置文本，按照次序，由application.properties、version-release-basic.yaml、version-release-1.yaml、version-release-2.yaml组成，中间用10个\"-\"组成换行分隔", required = true) String testConfig) {
        return doTest(testConfig, false);
    }

    private ResponseEntity<?> doTest(String testConfig, boolean testCaseConfigWithYaml) {
        try {
            simulatorConsoleResource.test(testConfig, testCaseConfigWithYaml);

            return ResponseUtil.getSuccessResponse(true);
        } catch (Exception e) {
            return ResponseUtil.getFailureResponse(e);
        }
    }
}