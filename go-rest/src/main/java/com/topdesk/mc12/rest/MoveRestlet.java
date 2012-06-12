package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

<<<<<<< HEAD
=======
import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.Game;
>>>>>>> Fixed guice, now done (part 2)
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.persistence.Backend;

@Path("move")
public class MoveRestlet {
	@Inject Backend backend;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
<<<<<<< HEAD
	public Move move(Move move) {
		getBackend().insert(move);
=======
	public Move move(Move move, @QueryParam("gameid") long gameId) {
		backend.insert(move);
		Board board = backend.get(Game.class, gameId).getBoard();
		board.getMoves().add(move);
		backend.update(board);
>>>>>>> Fixed guice, now done (part 2)
		return move;
	}
}
