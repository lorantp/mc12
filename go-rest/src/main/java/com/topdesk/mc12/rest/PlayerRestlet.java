package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;

@Path("player")
public class PlayerRestlet {
	@Inject private Backend backend;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Player get(@QueryParam("id") long id) {
		return backend.get(Player.class, id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Player post(Player player) {
		backend.insert(player);
		return player;
	}
}
