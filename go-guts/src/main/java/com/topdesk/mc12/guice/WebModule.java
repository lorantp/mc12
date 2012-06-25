package com.topdesk.mc12.guice;

import static com.google.inject.Scopes.*;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.topdesk.mc12.authentication.AuthorizationRequestFilter;
import com.topdesk.mc12.authentication.PlayerProvider;
import com.topdesk.mc12.authentication.SessionPlayerContextMap;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.RestInterfaceConfig;
import com.topdesk.mc12.rest.producers.GoExceptionMapper;
import com.topdesk.mc12.rest.producers.RuntimeExceptionMapper;
import com.topdesk.mc12.rest.restlets.ContextLoginRestlet;
import com.topdesk.mc12.rest.restlets.DefaultContextLoginRestlet;
import com.topdesk.mc12.rest.restlets.DefaultFacebookLoginRestlet;
import com.topdesk.mc12.rest.restlets.DefaultGameRestlet;
import com.topdesk.mc12.rest.restlets.FacebookLoginRestlet;
import com.topdesk.mc12.rest.restlets.GameRestlet;

// see http://code.google.com/p/google-guice/wiki/ServletModule
public class WebModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(RestInterfaceConfig.class);
		
		bind(RuntimeExceptionMapper.class);
		bind(GoExceptionMapper.class);
		
		bind(Player.class).toProvider(PlayerProvider.class);
		
		bind(PlayerContextMap.class).to(SessionPlayerContextMap.class);
		bind(AuthorizationRequestFilter.class);
		
		bind(GameRestlet.class).to(DefaultGameRestlet.class);
		
//		bind(LoginHelper.class); // why not needed?
		bind(ContextLoginRestlet.class).to(DefaultContextLoginRestlet.class);
		bind(FacebookLoginRestlet.class).to(DefaultFacebookLoginRestlet.class);
		
		bind(ObjectMapper.class).in(SINGLETON);
		
		serve("/rest/*").with(GuiceContainer.class, ImmutableMap.<String, String>builder()
				.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true")
				.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, AuthorizationRequestFilter.class.getName())
				.build());
	}
}
