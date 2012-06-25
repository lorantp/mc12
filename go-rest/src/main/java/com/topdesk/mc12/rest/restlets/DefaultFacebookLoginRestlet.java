package com.topdesk.mc12.rest.restlets;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

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

import com.google.common.base.Throwables;
import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@RequestScoped
@Slf4j
public class DefaultFacebookLoginRestlet implements FacebookLoginRestlet {
	private final PlayerContextMap contextMap;
	private OAuthService service;
	private ObjectMapper mapper;
	private final HttpServletRequest httpRequest;
	private final Provider<EntityManager> entityManager;
	
	@Inject 
	public DefaultFacebookLoginRestlet(PlayerContextMap contextMap, @Context HttpServletRequest httpRequest, Provider<EntityManager> entityManager) throws Exception {
		this.contextMap = contextMap;
		this.httpRequest = httpRequest;
		this.entityManager = entityManager;
		service = new ServiceBuilder()
				.provider(FacebookApi.class)
				.apiKey("256317947803042")
				.apiSecret("2d078c77b98e9be0d798d9f71b1669e6")
				.callback("http://mc12.topdesk.com/test/rest/login/facebook")
				.debug()
				.build();
	    mapper = new ObjectMapper();
	}
	
	@Override
	public Response login(String code) {
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(null, verifier);
		
	    OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
	    service.signRequest(accessToken, request);
	    org.scribe.model.Response response = request.send();

		try {
			JsonParser jp = mapper.getJsonFactory().createJsonParser(response.getBody());
			JsonNode data = mapper.readTree(jp);
			
			String playerName = data.get("username").asText();
			List<Player> players = selectByField("name", playerName, Player.class);
			
			int contextId;
			if (players.isEmpty()) {
				Player player = Player.create(playerName, playerName + "@topdesk.com");
				entityManager.get().persist(player);
				log.info("Created new player and logged in for {}", player);
				contextId = contextMap.startNew(player, httpRequest).hashCode();
			}
			else {
				Player player = players.get(0);
				if (contextMap.hasContextFor(player, httpRequest)) {
					log.info("Using existing login for {}", player);
					contextId = contextMap.getByPlayer(player, httpRequest).hashCode();
				}
				else {
					log.info("Logged in for {}", player);
					contextId = contextMap.startNew(player, httpRequest).hashCode();
				}
			}
			
			return Response.seeOther(URI.create("../"))
					.cookie(new NewCookie("contextId", Integer.toString(contextId), "/", null, null, -1, false))
					.build();
		} 
		catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entity) {
		EntityManager em = entityManager.get();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(entity);
		Root<E> root = query.from(entity);
		query.select(root).where(builder.equal(builder.upper(root.get(fieldName).as(String.class)), value.toUpperCase()));
		return em.createQuery(query).getResultList();
	}
}
/**
 * https://www.facebook.com/dialog/oauth?client_id=256317947803042&redirect_uri=http%3A%2F%2Flocalhost%2Frest%2Flogin%2Ffacebook
 * 
{"id":"100000280042812",
"name":"Bernd Der",
"first_name":"Bernd",
"last_name":"Der",
"link":"http:\/\/www.facebook.com\/derberndist",
"username":"derberndist",
"quotes":"\"So lang die dicke Frau noch singt, ist die Oper nicht zu Ende.\" Kettcar\n\n\u201cThere are things known, and there are things unknown, And in between are the Doors\u201d. Jim Morrison",
"gender":"male",
"timezone":2,
"locale":"de_DE",
"verified":true,
"updated_time":"2012-06-22T11:41:34+0000"}
*/