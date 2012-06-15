package com.topdesk.mc12.rest.restlets;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.Backend;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
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
	@Path("/{id}")
	public Game get(@PathParam("id") long id) {
		GameData gameData = backend.get(GameData.class, id);
		return ruleEngine.applyMoves(gameData);
	}
	
	@POST
	@Path("/pass")
	public void pass(RestPass pass) {
		GameData gameData = backend.get(GameData.class, pass.getGameId());
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, pass.getPlayerId());
		ruleEngine.applyPass(game, color);
		
		log.info("Player {} passed in game {}", pass.getPlayerId(), game);
		backend.insert(new Move(0, gameData, null, null, color));
	}
	
	@POST
	@Path("/move")
	public void move(RestMove restMove) {
		GameData gameData = backend.get(GameData.class, restMove.getGameId());
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, restMove.getPlayerId());
		game.addStone(restMove.getX(), restMove.getY(), color);
		
		log.info("Player {} made move {} in game {}", restMove.getPlayerId(), gameData);
		backend.insert(new Move(0, gameData, restMove.getX(), restMove.getY(), color));
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
