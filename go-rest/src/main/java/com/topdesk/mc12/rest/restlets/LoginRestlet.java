package com.topdesk.mc12.rest.restlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.rest.entities.ContextData;

@Path("context")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LoginRestlet {
	@GET @Path("/{name}") ContextData get(@Context HttpServletRequest request, @PathParam("name") String playerName);
	@GET @Path("/check/{id}") ContextData checkId(@Context HttpServletRequest request, @PathParam("id") String id);
}
