package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rest.entities.RestPass;

@Slf4j
@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRestlet {
	@Inject private Backend backend;
	
	@GET
	public Game get(@QueryParam("id") long id) {
		return fixRecursion(backend.get(Game.class, id));
	}
	
	@POST
	@Path("/pass")
	public void pass(RestPass pass) {
		Game game = backend.get(Game.class, pass.getGameId());
		Player player = getPlayer(game, pass.getPlayerId());
		checkTurn(game, player);
		Move newMove = new Move(0, game, null, null, player);
		log.info("Player {} passed in game {}", pass.getPlayerId(), game);
		backend.insert(newMove);
	}
	
	@POST
	@Path("/move")
	public void move(RestMove move) {
		Game game = backend.get(Game.class, move.getGameId());
		
		checkBounds(game.getBoardSize(), move.getX());
		checkBounds(game.getBoardSize(), move.getY());
		
		checkValidPosition(move, game);
		
		Player player = getPlayer(game, move.getPlayerId());
		checkTurn(game, player);
		log.info("Player {} made move {} in game {}", move.getPlayerId(), game);
		backend.insert(new Move(0, game, move.getX(), move.getY(), player));
	}
	
	private void checkValidPosition(RestMove move, Game game) {
		// We'll need to reimplement this when we handle capture and such
		for (Move m : game.getMoves()) {
			if (m.isPass()) {
				continue;
			}
			if (m.getX() == move.getX() && m.getY() == move.getY()) {
				throw new IllegalStateException("There's already a stone at " + m.getX() + ", " + m.getY());
			}
		}
	}
	
	private void checkTurn(Game game, Player player) {
		if (game.getMoves().size() % 2 == 0) {
			if (!player.equals(game.getBlack())) {
				throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
			}
		}
		else if (!player.equals(game.getWhite())) {
			throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
		}
	}
	
	private Player getPlayer(Game game, long playerId) {
		if (game.getBlack().getId() == playerId) {
			return game.getBlack();
		}
		else if (game.getWhite().getId() == playerId) {
			return game.getWhite();
		}
		throw new IllegalStateException("Player " + playerId + " does not participate in game " + game.getId());
	}
	
	private Game fixRecursion(Game game) {
//		for (Move move : game.getMoves()) {
//			move.setGame(null);
//		}
		return game;
	}
	
	private void checkBounds(BoardSize size, Integer coordinate) {
		if (coordinate == null) {
			throw new IllegalStateException("Got move without coordinate");
		}
		if (coordinate < 0 || coordinate >= size.getSize()) {
			throw new IllegalStateException("Move does not fit on board");
		}
	}
}
