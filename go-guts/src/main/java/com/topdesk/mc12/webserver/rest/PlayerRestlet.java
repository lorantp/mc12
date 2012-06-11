package com.topdesk.mc12.webserver.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.Player;

@Path("player")
public class PlayerRestlet {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Player get(@QueryParam("id") long id) {
		return new Player(id, "Jorn", "jornh@topdesk.com");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Player post(Player player) {
		System.err.println("Got " + player);
		return player;
	}
}
