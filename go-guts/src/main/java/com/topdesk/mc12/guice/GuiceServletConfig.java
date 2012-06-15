package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.topdesk.mc12.persistence.PersistenceModule;
import com.topdesk.mc12.testdata.TestData;

public class GuiceServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(
				new RuleModule(),
				new PersistenceModule(), 
				new WebModule(), 
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(TestData.class);
					}
				});
		injector.getInstance(TestData.class).create();
		return injector;
	}
}
