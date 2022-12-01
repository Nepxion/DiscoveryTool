package com.nepxion.discovery.automation.inspector.console.context;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.automation.common.console.context.ConsoleApplicationContextInitializer;

public class InspectorConsoleApplicationContextInitializer extends ConsoleApplicationContextInitializer {
    @Override
    public String getPluginName() {
        return "Inspector Test";
    }
}