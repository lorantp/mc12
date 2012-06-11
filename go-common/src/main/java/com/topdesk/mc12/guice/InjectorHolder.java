package com.topdesk.mc12.guice;

import com.google.inject.Injector;

public class InjectorHolder {
	private static Injector i;
	
	public static void init(Injector injector) {
		if (i != null) {
			throw new IllegalStateException();
		}
		i = injector;
	}
	
	public static Injector getInjector() {
		return i;
	}
}
