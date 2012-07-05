package com.topdesk.mc12.guice;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.topdesk.mc12.rest.restlets.DatabaseUpgrade;
import com.topdesk.mc12.testdata.TestData;

@Slf4j
public class GuiceServletConfig extends GuiceServletContextListener {
	private static final String PERSISTENCE_UNIT_LOCAL = "com.topdesk.mc12.go.local";
	private static final String PERSISTENCE_UNIT_PRODUCTION = "com.topdesk.mc12.go";
	
	private static final String LOCALHOST_PROPERTY = "go_test_on_localhost";
	private static final boolean IS_LOCALHOST = "true".equals(System.getProperty(LOCALHOST_PROPERTY));
	@Inject private PersistService service;
	
	@Override
	protected Injector getInjector() {
		String persistenceUnit = IS_LOCALHOST ? PERSISTENCE_UNIT_LOCAL : PERSISTENCE_UNIT_PRODUCTION;
		log.info("Using persistence unit {}", persistenceUnit);
		Injector injector = Guice.createInjector(
				new RuleModule(),
				new JpaPersistModule(persistenceUnit),
				new WebModule(),
				new AbstractModule() {
					@Override
					protected void configure() {
						bind(TestData.class);
						bind(DatabaseUpgrade.class);
					}
				});
		
		injector.injectMembers(this);
		service.start();
		
		// We only need test data in local since it's already in the production database.
		if (IS_LOCALHOST) {
			injector.getInstance(TestData.class).create();
		}
		injector.getInstance(DatabaseUpgrade.class).version0001();
		
		return injector;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		service.stop();
		super.contextDestroyed(servletContextEvent);
	}
}
