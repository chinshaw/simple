package com.simple.original.client;

import com.google.gwt.inject.client.GinModules;
import com.simple.original.client.dashboard.IOCDashboardModule;

@GinModules({ IOCDesktopModule.class, IOCServicesModule.class, IOCDashboardModule.class })
public interface IOCDesktopInjector extends IOCBaseInjector {

}
