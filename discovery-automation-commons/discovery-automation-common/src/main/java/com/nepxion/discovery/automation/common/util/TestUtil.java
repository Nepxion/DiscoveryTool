package com.nepxion.discovery.automation.common.util;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.exception.DiscoveryException;
import com.nepxion.discovery.common.property.DiscoveryContent;
import com.nepxion.discovery.common.util.RestUtil;
import com.nepxion.discovery.common.util.UrlUtil;

public class TestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

    public static String formatContextPath(String contextPath) {
        return UrlUtil.formatContextPath(contextPath);
    }

    public static String getContent(String path) {
        try {
            DiscoveryContent discoveryContent = new DiscoveryContent(path, DiscoveryConstant.ENCODING_UTF_8);

            return discoveryContent.getContent();
        } catch (IOException e) {
            throw new DiscoveryException(e);
        }
    }

    public static void determineRestException(TestRestTemplate testRestTemplate) {
        String error = RestUtil.getError(testRestTemplate.getRestTemplate());
        if (StringUtils.isNotEmpty(error)) {
            DiscoveryException discoveryException = new DiscoveryException(error);

            LOG.error("RestTemplate调用异常", discoveryException);

            throw discoveryException;
        }
    }
}