package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;

@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameStateRestlet {
	@GET
	public Game get(@QueryParam("id") long id) {
		return new Game(
				id, 
				new Board(1, BoardSize.NINETEEN.getSize(), ImmutableList.of(
						new Move(1, 3, 3, Color.BLACK),
						new Move(2, 2, 3, Color.WHITE),
						new Move(3, 4, 2, Color.BLACK))), 
				new Player(1, "Jorn", "jornh@topdesk.com"), 
				new Player(2, "Bernd", "berndj@topdesk.com"), 
				42, -1);
	}
}
