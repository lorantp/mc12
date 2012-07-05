package com.topdesk.mc12.rest.restlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.topdesk.mc12.rest.entities.ContextData;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LoginRestlet {
	@GET @Path("/check/{id}") ContextData checkId(@Context HttpServletRequest request, @PathParam("id") String id);
	
	@GET @Path("/unsafe/{name}") ContextData unsafeLogin(@PathParam("name") String playerName);
	
	@GET @Path("/facebook") Response facebookLogin(@QueryParam("code") String code);
	@GET @Path("/twitter/forward") Response twitterLoginForward();
	@GET @Path("/twitter") String twitterLogin(@QueryParam("oauth_token") String token, @QueryParam("oauth_verifier") String code);
	@GET @Path("/google/forward") Response googleLoginForward();
	@GET @Path("/google") String googleLogin(@QueryParam("oauth_token") String token);
}
