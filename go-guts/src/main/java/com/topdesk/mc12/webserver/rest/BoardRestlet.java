package com.topdesk.mc12.webserver.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;

@Path("board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardRestlet {
	@GET
	public Board get(@QueryParam("id") long id) {
		return new Board(4l, new Game(), ImmutableList.<Move>of(new Move(4l, 4, 3, Color.BLACK), new Move(4l, 4, 4, Color.WHITE)));
	}
}
