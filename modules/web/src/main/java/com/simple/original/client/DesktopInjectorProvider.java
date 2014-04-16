package com.simple.original.client;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Provider;

public class DesktopInjectorProvider implements InjectorProvider, Provider<IOCDesktopInjector> {

	public IOCDesktopInjector get() {
		return GWT.create(IOCDesktopInjector.class);
	}
}
