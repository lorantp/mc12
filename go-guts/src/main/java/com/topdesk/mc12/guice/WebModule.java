package com.topdesk.mc12.guice;

import static com.google.inject.Scopes.SINGLETON;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.topdesk.mc12.authentication.AuthorizationRequestFilter;
import com.topdesk.mc12.authentication.DefaultSessionMap;
import com.topdesk.mc12.authentication.PlayerProvider;
import com.topdesk.mc12.authentication.SessionMap;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.RestInterfaceConfig;
import com.topdesk.mc12.rest.producers.GoExceptionMapper;
import com.topdesk.mc12.rest.producers.RuntimeExceptionMapper;
import com.topdesk.mc12.rest.restlets.DefaultGameRestlet;
import com.topdesk.mc12.rest.restlets.GameRestlet;

// see http://code.google.com/p/google-guice/wiki/ServletModule
public class WebModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(RestInterfaceConfig.class);
		
		bind(RuntimeExceptionMapper.class).in(SINGLETON);
		bind(GoExceptionMapper.class).in(SINGLETON);
		
		bind(Player.class).toProvider(PlayerProvider.class);
		
		bind(SessionMap.class).to(DefaultSessionMap.class).in(SINGLETON);
		bind(AuthorizationRequestFilter.class).in(SINGLETON);
		bind(GameRestlet.class).to(DefaultGameRestlet.class);
		
		serve("/rest/*").with(GuiceContainer.class, ImmutableMap.<String, String> builder()
				.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
				.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, AuthorizationRequestFilter.class.getName())
				.build());
	}
}
