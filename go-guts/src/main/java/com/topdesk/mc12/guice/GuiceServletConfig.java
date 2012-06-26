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
	private static final String PERSISTENCE_UNIT_PRODUCTION = "com.topdesk.mc12.go";
	private static final String PERSISTENCE_UNIT = System.getProperty("GoPersistence", PERSISTENCE_UNIT_PRODUCTION);
	@Inject private PersistService service;
	
	@Override
	protected Injector getInjector() {
		log.info("Using persistence unit {}", PERSISTENCE_UNIT);
		Injector injector = Guice.createInjector(
				new RuleModule(),
				new JpaPersistModule(PERSISTENCE_UNIT),
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
		if (!PERSISTENCE_UNIT_PRODUCTION.equals(PERSISTENCE_UNIT)) {
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
