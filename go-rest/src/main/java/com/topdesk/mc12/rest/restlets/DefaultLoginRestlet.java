package com.topdesk.mc12.rest.restlets;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

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
import org.scribe.builder.api.Api;
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
public class DefaultLoginRestlet implements LoginRestlet {
	private static final String LOCALHOST_PROPERTY = "go_test_on_localhost";
	private static final boolean IS_LOCALHOST = "true".equals(System.getProperty(LOCALHOST_PROPERTY));
	
	private final HttpServletRequest request;
	private final LoginHelper loginHelper;
	private final ObjectMapper mapper;
	
	private final OAuthService facebookService;
	private final OAuthService googleService;
	private final OAuthService twitterService;
	
	@Inject
	public DefaultLoginRestlet(@Context HttpServletRequest request, ObjectMapper mapper, LoginHelper loginHelper) {
		this.request = request;
		this.mapper = mapper;
		this.loginHelper = loginHelper;
		
		Properties properties = loadProperties();
		this.facebookService = createServiceBuilder(FacebookApi.class, properties, "facebook").build();
		this.googleService = createServiceBuilder(GoogleApi.class, properties, "google")
				.scope("https://apps-apis.google.com/a/feeds/user/#readonly").build();
		this.twitterService = createServiceBuilder(TwitterApi.class, properties, "twitter").build();
	}
	
	private static ServiceBuilder createServiceBuilder(Class<? extends Api> apiType, Properties properties, String authKey) {
		String keyPrefix = authKey + (IS_LOCALHOST ? ".local." : ".test.");
		return new ServiceBuilder()
				.provider(apiType)
				.apiKey(properties.getProperty(keyPrefix + "key"))
				.apiSecret(properties.getProperty(keyPrefix + "secret"))
				.callback(properties.getProperty(keyPrefix + "callback"));
	}
	
	@SneakyThrows(IOException.class)
	private static Properties loadProperties() {
		Properties properties = new Properties();
		properties.load(DefaultLoginRestlet.class.getResourceAsStream("/login.properties"));
		return properties;
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
