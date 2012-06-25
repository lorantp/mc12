package com.topdesk.mc12.rest.restlets;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
@RequestScoped
public class DefaultFacebookLoginRestlet implements FacebookLoginRestlet {
	private final HttpServletRequest httpRequest;
	private final OAuthService service;
	private final ObjectMapper mapper;
	private final LoginHelper loginHelper;
	
	@Inject
	public DefaultFacebookLoginRestlet(PlayerContextMap contextMap, @Context HttpServletRequest httpRequest, Provider<EntityManager> entityManager, ObjectMapper mapper, LoginHelper loginHelper) {
		this.httpRequest = httpRequest;
		this.service = new ServiceBuilder()
				.provider(FacebookApi.class)
				.apiKey("256317947803042")
				.apiSecret("2d078c77b98e9be0d798d9f71b1669e6")
				.callback("http://localhost/rest/login/facebook")
				.debug()
				.build();
		
		this.mapper = mapper;
		this.loginHelper = loginHelper;
	}
	
	@Override
	public Response login(String code) {
		String response = verify(code);
		JsonNode data = parse(response);
		if (!data.get("verified").asBoolean()) {
			throw GoException.createUnauthorized("Facebook login rejected");
		}
		
		String playerName = data.get("username").asText();
		Player player = loginHelper.getOrCreate(playerName);
		PlayerContext context = loginHelper.login(player, httpRequest);
		String contextId = Integer.toString(context.hashCode());
		return Response.seeOther(URI.create("../"))
				.cookie(new NewCookie("contextId", contextId, "/", null, null, -1, false))
				.build();
	}
	
	@SneakyThrows(IOException.class)
	private JsonNode parse(String jsonString) {
		JsonParser parser = mapper.getJsonFactory().createJsonParser(jsonString);
		return mapper.readTree(parser);
	}
	
	private String verify(String code) {
		Token accessToken = service.getAccessToken(null, new Verifier(code));
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
		service.signRequest(accessToken, request);
		log.debug("Verifying login with Facebook server");
		return request.send().getBody();
	}
}
