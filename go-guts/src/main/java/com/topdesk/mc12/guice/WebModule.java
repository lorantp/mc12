package com.topdesk.mc12.guice;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.topdesk.mc12.rest.BoardRestlet;
import com.topdesk.mc12.rest.GameStateRestlet;
import com.topdesk.mc12.rest.MoveRestlet;
import com.topdesk.mc12.rest.PlayerRestlet;
import com.topdesk.mc12.rest.RestInterfaceConfig;

// see http://code.google.com/p/google-guice/wiki/ServletModule
public class WebModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(RestInterfaceConfig.class);
		
		bind(GameStateRestlet.class);
		bind(BoardRestlet.class);
		bind(MoveRestlet.class);
		bind(PlayerRestlet.class);
		
		serve("/rest/*").with(GuiceContainer.class, ImmutableMap.<String, String> builder()
				.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
				.build());
	}
}
