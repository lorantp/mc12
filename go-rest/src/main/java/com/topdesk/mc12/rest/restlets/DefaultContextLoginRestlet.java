package com.topdesk.mc12.rest.restlets;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.scribe.builder.api.TwitterApi;
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
	private final OAuthService twitterService;
	
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
				.build();
		
		this.googleService = new ServiceBuilder()
				.provider(GoogleApi.class)
				.apiKey("73477428485.apps.googleusercontent.com")
				.apiSecret("xLZFOen9KvQItfQKFV__eVca")
				.callback("http://mc12.topdesk.com/test/rest/login/google")
				.scope("https://apps-apis.google.com/a/feeds/user/#readonly")
				.debug()
				.build();
		
		this.twitterService = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey("666p5JUyzJy9CdnE9wNTQ")
				.apiSecret("JnNmoZS6FDYnKUQw8toDrz6pTDOjEifLPHFr1MkuWY")
				.callback("http://mc12.topdesk.com/test/rest/login/twitter")
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
	
	@Override
	public Response twitterLoginForward() {
		Token requestToken = twitterService.getRequestToken();
		request.getSession(true).setAttribute("twitterToken", requestToken);
		String url = twitterService.getAuthorizationUrl(requestToken);
		return Response.seeOther(URI.create(url)).build();
	}
	
	@Override
	public String twitterLogin(String token, String code) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw GoException.createUnauthorized("No token");
		}
		
		Token requestToken = (Token) session.getAttribute("twitterToken");
		if (requestToken == null) {
			throw GoException.createUnauthorized("No token");
		}
		session.removeAttribute("twitterToken");
		
		Token accessToken = twitterService.getAccessToken(requestToken, new Verifier(code));
		OAuthRequest authRequest = new OAuthRequest(Verb.GET, "https://twitter.com/account/verify_credentials");
		twitterService.signRequest(accessToken, authRequest);
		String response = authRequest.send().getBody();
		log.warn(response);
		return response;
		
//		Player player = loginHelper.getOrCreate(parse(response).get("username").asText());
//		PlayerContext context = loginHelper.login(player, request);
//		String contextId = Integer.toString(context.hashCode());
//		return Response
//				.seeOther(URI.create("../"))
//				.cookie(new NewCookie("contextId", contextId, "/", null, null, -1, false))
//				.build();
	}
	
	private ContextData createData(PlayerContext context) {
		return new ContextData(context.hashCode(), context.getPlayer().getId());
	}
	
	@Override
	public Response facebookLogin(String code) {
		Token accessToken = facebookService.getAccessToken(null, new Verifier(code));
		OAuthRequest authRequest = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
		facebookService.signRequest(accessToken, authRequest);
		log.debug("Verifying login with Facebook server");
		String response = authRequest.send().getBody();
		
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
	
	@Override
	public Response googleLoginForward() {
		Token requestToken = googleService.getRequestToken();
		request.getSession(true).setAttribute("googleToken", requestToken);
		String url = googleService.getAuthorizationUrl(requestToken);
		return Response.seeOther(URI.create(url)).build();
	}
	
	@Override
	public String googleLogin(String token) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw GoException.createUnauthorized("No token");
		}
		
		Token requestToken = (Token) session.getAttribute("googleToken");
		if (requestToken == null) {
			throw GoException.createUnauthorized("No token");
		}
		session.removeAttribute("googleToken");
		
		Token accessToken = googleService.getAccessToken(requestToken, new Verifier(token));
		OAuthRequest authRequest = new OAuthRequest(Verb.GET, "https://apps-apis.google.com/a/feeds/user/");
		googleService.signRequest(accessToken, authRequest);
		String response = authRequest.send().getBody();
		System.err.println(response);
		
		// TODO finish
		return response;
	}
	
	@SneakyThrows(IOException.class)
	private JsonNode parse(String jsonString) {
		JsonParser parser = mapper.getJsonFactory().createJsonParser(jsonString);
		return mapper.readTree(parser);
	}
}
