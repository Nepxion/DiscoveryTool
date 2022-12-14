package com.nepxion.discovery.automation.inspector.console.endpoint;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nepxion.discovery.automation.inspector.console.resource.InspectorConsoleResource;
import com.nepxion.discovery.common.util.ResponseUtil;

@RestController
@RequestMapping(path = "/inspector-test")
public class InspectorConsoleEndpoint {
    @Autowired
    private InspectorConsoleResource inspectorConsoleResource;

    @RequestMapping(path = "/test-config-yaml", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> testConfigYaml(@RequestBody String testConfig) {
        return doTest(testConfig, true);
    }

    @RequestMapping(path = "/test-config-properties", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> testConfigProperties(@RequestBody String testConfig) {
        return doTest(testConfig, false);
    }

    private ResponseEntity<?> doTest(String testConfig, boolean testCaseConfigWithYaml) {
        try {
            String result = inspectorConsoleResource.test(testConfig, testCaseConfigWithYaml);

            return ResponseUtil.getSuccessResponse(result);
        } catch (Exception e) {
            return ResponseUtil.getFailureResponse(e);
        }
    }
}