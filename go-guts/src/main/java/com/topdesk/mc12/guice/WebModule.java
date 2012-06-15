package com.topdesk.mc12.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Scopes;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.topdesk.mc12.rest.GameRestlet;
import com.topdesk.mc12.rest.GoExceptionMapper;
import com.topdesk.mc12.rest.RestInterfaceConfig;
import com.topdesk.mc12.rest.RuntimeExceptionMapper;
import com.topdesk.mc12.rules.DefaultGoRuleEngine;
import com.topdesk.mc12.rules.GoRuleEngine;

// see http://code.google.com/p/google-guice/wiki/ServletModule
public class WebModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(RestInterfaceConfig.class);
		
		bind(RuntimeExceptionMapper.class).in(Scopes.SINGLETON);
		bind(GoExceptionMapper.class).in(Scopes.SINGLETON);
		
		bind(GameRestlet.class);
		
		bind(GoRuleEngine.class).to(DefaultGoRuleEngine.class);
		
		serve("/rest/*").with(GuiceContainer.class, ImmutableMap.<String, String> builder()
				.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
				.build());
	}
}
