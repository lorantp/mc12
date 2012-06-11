package com.topdesk.mc12.guice;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;

// see http://code.google.com/p/google-guice/wiki/ServletModule
public class WebModule extends ServletModule {
	@Override
	protected void configureServlets() {
		PackagesResourceConfig config = new PackagesResourceConfig("com.topdesk.mc12.rest");
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		serve("/rest/*").with(new ServletContainer(config));
	}
}
