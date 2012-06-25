package com.topdesk.mc12.rest.restlets;

import java.io.IOException;
import java.net.URI;

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
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.ContextData;

@Slf4j
@RequestScoped
public class DefaultContextLoginRestlet implements LoginRestlet {
	private final HttpServletRequest request;
	private final LoginHelper loginHelper;
	private final ObjectMapper mapper;
	
	private final OAuthService facebookService;
	private final OAuthService googleService;
	
	@Inject
	public DefaultContextLoginRestlet(@Context HttpServletRequest request, ObjectMapper mapper, LoginHelper loginHelper) {
		this.request = request;
		
		this.mapper = mapper;
		this.loginHelper = loginHelper;
		
		this.facebookService = new ServiceBuilder()
				.provider(FacebookApi.class)
				.apiKey("256317947803042")
				.apiSecret("2d078c77b98e9be0d798d9f71b1669e6")
				.callback("http://mc12.topdesk.com/test/rest/login/facebook")
				.debug()
				.build();
		
		this.googleService = new ServiceBuilder()
				.provider(GoogleApi.class)
				.apiKey("73477428485.apps.googleusercontent.com")
				.apiSecret("xLZFOen9KvQItfQKFV__eVca")
				.callback("http://mc12.topdesk.com/test/rest/login/google")
				.scope("https://docs.google.com/feeds")
				.debug()
				.build();
	}
	
	@Override
	public ContextData checkId(HttpServletRequest request, String id) {
		PlayerContext context = loginHelper.checkLogin(id, request);
		return createData(context);
	}
	
	@Override
	public ContextData unsafeLogin(String playerName) {
		Player player = loginHelper.getOrCreate(playerName);
		PlayerContext context = loginHelper.login(player, request);
		return createData(context);
	}
	
	private ContextData createData(PlayerContext context) {
		return new ContextData(context.hashCode(), context.getPlayer().getId());
	}
	
	@Override
	public Response facebookLogin(String code) {
		String response = verifyFacebook(code);
		JsonNode data = parse(response);
		if (!data.get("verified").asBoolean()) {
			throw GoException.createUnauthorized("Facebook login rejected");
		}
		
		String playerName = data.get("username").asText();
		Player player = loginHelper.getOrCreate(playerName);
		PlayerContext context = loginHelper.login(player, request);
		String contextId = Integer.toString(context.hashCode());
		return Response
				.seeOther(URI.create("../"))
				.cookie(new NewCookie("contextId", contextId, "/", null, null, -1, false))
				.build();
	}
	
	private String verifyFacebook(String code) {
		Token accessToken = facebookService.getAccessToken(null, new Verifier(code));
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
		facebookService.signRequest(accessToken, request);
		log.debug("Verifying login with Facebook server");
		return request.send().getBody();
	}
	
	@Override
	public Response googleLoginRedirect() {
		Token requestToken = googleService.getRequestToken();
		request.getSession().setAttribute("googleToken", requestToken);
		System.err.println("Storing token in session: " + requestToken);
		String url = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=" + requestToken.getToken();
		return Response.seeOther(URI.create(url)).build();
	}
	
	@Override
	public Response googleLogin(String code) {
		Token requestToken = (Token) request.getSession().getAttribute("googleToken");
		System.err.println("Got token " + requestToken);
		if (requestToken == null) {
			throw GoException.createUnauthorized("No token");
		}
		Token accessToken = googleService.getAccessToken(requestToken, new Verifier(code));
		OAuthRequest authRequest = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo");
		googleService.signRequest(accessToken, authRequest);
		log.debug("Verifying login with Google server");
		
		String response = authRequest.send().getBody();
		System.err.println("Response: " + response);
		JsonNode data = parse(response);
		if (!data.get("verified").asBoolean()) {
			throw GoException.createUnauthorized("Google login rejected");
		}
		
		String playerName = data.get("username").asText();
		Player player = loginHelper.getOrCreate(playerName);
		PlayerContext context = loginHelper.login(player, request);
		String contextId = Integer.toString(context.hashCode());
		return Response
				.seeOther(URI.create("../"))
				.cookie(new NewCookie("contextId", contextId, "/", null, null, -1, false)).build();
	}
	
	@SneakyThrows(IOException.class)
	private JsonNode parse(String jsonString) {
		JsonParser parser = mapper.getJsonFactory()
				.createJsonParser(jsonString);
		return mapper.readTree(parser);
	}
}
