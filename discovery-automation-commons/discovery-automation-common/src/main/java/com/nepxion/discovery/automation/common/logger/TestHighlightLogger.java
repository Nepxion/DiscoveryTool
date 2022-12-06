package com.nepxion.discovery.automation.common.logger;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.nepxion.discovery.common.logback.LogbackConstant;

public abstract class TestHighlightLogger {
    public void testHighlightBlack(String format, Object... arguments) {
        testHighlight(LogbackConstant.BLACK_FG, format, arguments);
    }

    public void testHighlightRed(String format, Object... arguments) {
        testHighlight(LogbackConstant.RED_FG, format, arguments);
    }

    public void testHighlightGreen(String format, Object... arguments) {
        testHighlight(LogbackConstant.GREEN_FG, format, arguments);
    }

    public void testHighlightYellow(String format, Object... arguments) {
        testHighlight(LogbackConstant.YELLOW_FG, format, arguments);
    }

    public void testHighlightBlue(String format, Object... arguments) {
        testHighlight(LogbackConstant.BLUE_FG, format, arguments);
    }

    public void testHighlightMagenta(String format, Object... arguments) {
        testHighlight(LogbackConstant.MAGENTA_FG, format, arguments);
    }

    public void testHighlightCyan(String format, Object... arguments) {
        testHighlight(LogbackConstant.CYAN_FG, format, arguments);
    }

    public void testHighlightWhite(String format, Object... arguments) {
        testHighlight(LogbackConstant.WHITE_FG, format, arguments);
    }

    public void testHighlightDefault(String format, Object... arguments) {
        testHighlight(LogbackConstant.DEFAULT_FG, format, arguments);
    }

    public void testHighlight(String color, String format, Object... arguments) {
        MDC.put(LogbackConstant.ANSI_COLOR, color);
        getLogger().info(format, arguments);
        MDC.remove(LogbackConstant.ANSI_COLOR);
    }

    public abstract Logger getLogger();
}