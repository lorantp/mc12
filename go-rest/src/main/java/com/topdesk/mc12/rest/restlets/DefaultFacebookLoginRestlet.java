package com.topdesk.mc12.rest.restlets;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
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

@RequestScoped
public class DefaultFacebookLoginRestlet implements FacebookLoginRestlet {
	private OAuthService service;
	private ObjectMapper mapper;

	public DefaultFacebookLoginRestlet() throws Exception {
		service = new ServiceBuilder()
				.provider(FacebookApi.class)
				.apiKey("256317947803042")
				.apiSecret("2d078c77b98e9be0d798d9f71b1669e6")
				.callback("http://localhost/rest/login/facebook")
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
			System.out.println(data.get("username"));
		} 
		catch (Exception e) {
			Throwables.propagate(e);
		}
	    
		return Response.seeOther(URI.create("../")).build();
	}

}
/**
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