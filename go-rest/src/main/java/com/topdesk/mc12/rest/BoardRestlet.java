package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.persistence.Backend;

@Path("board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardRestlet {
	@Inject private Backend backend;
	
	@GET
	public Board get(@QueryParam("id") long id) {
		return fixRecursion(backend.get(Board.class, id));
	}
	
	/**
	 * Until we can convince the auto-JSON thingy to exclude the board field in move, we need to null it before sending
	 */
	public static Board fixRecursion(Board board) {
		for (Move move : board.getMoves()) {
			move.setBoard(null);
		}
		return board;
	}
}
