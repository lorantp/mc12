package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.persistence.Backend;

@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameStateRestlet {
	@Inject Backend backend;
	
	@GET
	public Game get(@QueryParam("id") long id) {
		return backend.get(Game.class, id);
	}
}
