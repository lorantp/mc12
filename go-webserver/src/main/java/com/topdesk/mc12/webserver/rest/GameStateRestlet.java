package com.topdesk.mc12.webserver.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Player;

@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameStateRestlet {
	@GET
	public Game get(@QueryParam("id") long id) {
		return new Game(id, BoardSize.NINETEEN, new Player(1, "Jorn"), new Player(2, "Bernd"), 42, -1);
	}
}
