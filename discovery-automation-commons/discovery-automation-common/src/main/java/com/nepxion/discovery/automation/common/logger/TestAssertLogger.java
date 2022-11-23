package com.nepxion.discovery.automation.common.logger;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.slf4j.Logger;

public class TestAssertLogger {
    public static void assertEquals(Logger logger, String message, long expected, long actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            logger.error(message, e);

            throw e;
        }
    }

    public static void assertEquals(Logger logger, String message, Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable e) {
            logger.error(message, e);

            throw e;
        }
    }

    public static <T> void assertThat(Logger logger, String message, T actual, Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(message, actual, matcher);
        } catch (Throwable e) {
            logger.error(message, e);

            throw e;
        }
    }
}