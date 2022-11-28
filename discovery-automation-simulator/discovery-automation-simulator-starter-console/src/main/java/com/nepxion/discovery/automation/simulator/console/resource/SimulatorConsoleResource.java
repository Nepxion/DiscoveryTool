package com.nepxion.discovery.automation.simulator.console.resource;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import com.nepxion.discovery.automation.common.console.resource.ConsoleResource;

public interface SimulatorConsoleResource extends ConsoleResource {
    List<String> getRunningTestCases();
}