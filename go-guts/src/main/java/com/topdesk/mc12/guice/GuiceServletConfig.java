package com.topdesk.mc12.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.topdesk.mc12.persistence.PersistenceModule;

public class GuiceServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new PersistenceModule(), new WebModule());
	}
}
