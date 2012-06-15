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
import com.topdesk.mc12.persistence.Backend;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rest.entities.RestPass;
import com.topdesk.mc12.rules.RuleEngine;
import com.topdesk.mc12.rules.entities.Game;


@Slf4j
@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRestlet {
	@Inject private Backend backend;
	@Inject private RuleEngine ruleEngine;
	
	@GET
	public Game get(@QueryParam("id") long id) {
		GameData game = backend.get(GameData.class, id);
		return ruleEngine.turnMovesIntoBoard(game);
	}
	
	@POST
	@Path("/pass")
	public void pass(RestPass pass) {
		GameData game = backend.get(GameData.class, pass.getGameId());
		Player player = getPlayer(game, pass.getPlayerId());
		checkTurn(game, player);
		Move newMove = new Move(0, game, null, null, player);
		log.info("Player {} passed in game {}", pass.getPlayerId(), game);
		backend.insert(newMove);
	}
	
	@POST
	@Path("/move")
	public void move(RestMove restMove) {
		GameData gameData = backend.get(GameData.class, restMove.getGameId());
		
		ruleEngine.checkBounds(gameData.getBoardSize(), restMove.getX());
		ruleEngine.checkBounds(gameData.getBoardSize(), restMove.getY());
		
		Player player = getPlayer(gameData, restMove.getPlayerId());
		Move move = new Move(0, gameData, restMove.getX(), restMove.getY(), player);
		ruleEngine.checkValidPosition(move, gameData);
		ruleEngine.checkTurn(gameData, player);
		
		log.info("Player {} made move {} in game {}", restMove.getPlayerId(), gameData);
		backend.insert(new Move(0, gameData, restMove.getX(), restMove.getY(), player));
	}
	
//	private void checkValidPosition(RestMove move, Game game) {
//		// We'll need to reimplement this when we handle capture and such
//		for (Move m : game.getMoves()) {
//			if (m.isPass()) {
//				continue;
//			}
//			if (m.getX() == move.getX() && m.getY() == move.getY()) {
//				throw new IllegalStateException("There's already a stone at " + m.getX() + ", " + m.getY());
//			}
//		}
//	}
	
	private Player getPlayer(GameData game, long playerId) {
		if (game.getBlack().getId() == playerId) {
			return game.getBlack();
		}
		else if (game.getWhite().getId() == playerId) {
			return game.getWhite();
		}
		throw new IllegalStateException("Player " + playerId + " does not participate in game " + game.getId());
	}
	
	private void checkTurn(GameData game, Player player) {
		if (game.getMoves().size() % 2 == 0) {
			if (!player.equals(game.getBlack())) {
				throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
			}
		}
		else if (!player.equals(game.getWhite())) {
			throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
		}
	}

	private GameData fixRecursion(GameData game) {
//		for (Move move : game.getMoves()) {
//			move.setGame(null);
//		}
		return game;
	}
}
