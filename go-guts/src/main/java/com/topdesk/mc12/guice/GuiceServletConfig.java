package com.topdesk.mc12.guice;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.topdesk.mc12.testdata.TestData;

public class GuiceServletConfig extends GuiceServletContextListener {
	@Inject private PersistService service;
	
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(
				new RuleModule(),
				new JpaPersistModule("com.topdesk.mc12.go"),
				new WebModule(),
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(TestData.class);
					}
				});
		injector.injectMembers(this);
		service.start();
		injector.getInstance(TestData.class).create();
		return injector;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		service.stop();
		super.contextDestroyed(servletContextEvent);
	}
}
