package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.persistence.Backend;

@Path("move")
public class MoveRestlet {
	@Inject Backend backend;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Board move(Move move, @QueryParam("boardid") long boardId) {
		Board board = backend.get(Board.class, boardId);
		move.setBoard(board);
		backend.insert(move);
		return BoardRestlet.fixRecursion(board);
	}
}
