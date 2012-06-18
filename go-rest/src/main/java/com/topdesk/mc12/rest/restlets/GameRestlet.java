package com.topdesk.mc12.rest.restlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.Backend;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.NewGame;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rest.entities.RestPass;
import com.topdesk.mc12.rest.entities.StartGame;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.entities.Game;

@Slf4j
@Path("game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameRestlet {
	@Inject private Backend backend;
	@Inject private GoRuleEngine ruleEngine;
	
	@GET
	@Path("/{id}")
	public Game get(@PathParam("id") long gameId) {
		GameData gameData = backend.get(GameData.class, gameId);
		return ruleEngine.applyMoves(gameData);
	}
	
	@POST
	@Path("/pass")
	public void pass(RestPass pass) {
		GameData gameData = backend.get(GameData.class, pass.getGameId());
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, pass.getPlayerId());
		ruleEngine.applyPass(game, color);
		
		if (game.isFinished()) {
			gameData.setState(GameState.FINISHED);
			backend.update(gameData);
		}
		
		log.info("Player {} passed in game {}", pass.getPlayerId(), game);
		backend.insert(new Move(0, gameData, null, null, color));
	}
	
	@POST
	@Path("/move")
	public void move(RestMove restMove) {
		GameData gameData = backend.get(GameData.class, restMove.getGameId());
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, restMove.getPlayerId());
		ruleEngine.applyMove(game, color, restMove.getX(), restMove.getY());
		
		log.info("Player {} made move {} in game {}", restMove.getPlayerId(), gameData);
		backend.insert(new Move(0, gameData, restMove.getX(), restMove.getY(), color));
	}
	
	@POST
	@Path("/new")
	public long newGame(NewGame newGame) {
		Player player = backend.get(Player.class, newGame.getInitiatedPlayerId());
		GameData game = new GameData(0, null, null, null, new DateTime().getMillis(), BoardSize.get(newGame.getBoardSize()), GameState.INITIATED);
		if (newGame.getColor() == Color.BLACK) {
			game.setBlack(player);
		}
		else {
			game.setWhite(player);
		}
		backend.insert(game);
		return game.getId();
	}
	
	@POST
	@Path("/start/{id}")
	public void startGame(StartGame startGame, @PathParam("id") long gameId) {
		GameData gameData = backend.get(GameData.class, gameId);
		checkInitiated(gameData);
		
		Player initiated = gameData.getBlack() == null ? gameData.getWhite() : gameData.getBlack();
		if (startGame.getOtherPlayerId() == initiated.getId()) {
			throw GoException.createNotAcceptable("Can't play against yourself");
		}
		
//		if (gam)
		gameData.setState(GameState.CANCELLED);
	}
	
	@POST
	@Path("/cancel/{id}")
	public void cancelGame(StartGame startGame, @PathParam("id") long gameId) {
		GameData gameData = backend.get(GameData.class, gameId);
		checkInitiated(gameData);
		gameData.setState(GameState.CANCELLED);
		backend.update(gameData);
	}
	
	private void checkInitiated(GameData gameData) {
		if (gameData.getState() != GameState.INITIATED) {
			throw GoException.createNotAcceptable("Game is not in INITIATED state");
		}
	}
	
	private Color getPlayerColor(GameData game, long playerId) {
		if (game.getBlack().getId() == playerId) {
			return Color.BLACK;
		}
		else if (game.getWhite().getId() == playerId) {
			return Color.WHITE;
		}
		throw GoException.createNotAcceptable("Player " + playerId + " does not participate in game " + game.getId());
	}
}
