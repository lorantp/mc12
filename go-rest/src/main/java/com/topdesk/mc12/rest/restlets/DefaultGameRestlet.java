package com.topdesk.mc12.rest.restlets;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.NewGame;
import com.topdesk.mc12.rest.entities.PlayerId;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.entities.Game;

@Slf4j
@Transactional
public class DefaultGameRestlet implements GameRestlet {
	@Inject private Provider<EntityManager> entityManager;
	@Inject private GoRuleEngine ruleEngine;
	
	@Override
	public Game get(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		return ruleEngine.applyMoves(gameData);
	}
	
	@Override
	public void pass(long gameId, PlayerId playerId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, playerId.getPlayerId());
		ruleEngine.applyPass(game, color);
		
		if (game.isFinished()) {
			gameData.setState(GameState.FINISHED);
			entityManager.get().merge(gameData);
		}
		
		log.info("Player {} passed in game {}", playerId.getPlayerId(), game);
		entityManager.get().persist(new Move(0, gameData, null, null, color));
	}
	
	@Override
	public void move(long gameId, RestMove restMove) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, restMove.getPlayerId());
		ruleEngine.applyMove(game, color, restMove.getX(), restMove.getY());
		
		log.info("Player {} made move {} in game {}", restMove.getPlayerId(), gameData);
		entityManager.get().persist(new Move(0, gameData, restMove.getX(), restMove.getY(), color));
	}
	
	@Override
	public long newGame(NewGame newGame) {
		Player player = entityManager.get().find(Player.class, newGame.getPlayerId());
		GameData game = new GameData(0, null, null, null, new DateTime().getMillis(), BoardSize.get(newGame.getBoardSize()), GameState.INITIATED);
		if (newGame.getColor() == Color.BLACK) {
			game.setBlack(player);
		}
		else {
			game.setWhite(player);
		}
		entityManager.get().persist(game);
		return game.getId();
	}
	
	@Override
	public void startGame(long gameId, PlayerId playerId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		
		Player initiated = gameData.getBlack() == null ? gameData.getWhite() : gameData.getBlack();
		if (playerId.getPlayerId() == initiated.getId()) {
			throw GoException.createNotAcceptable("Can't play against yourself");
		}
		
		gameData.setState(GameState.CANCELLED);
	}
	
	@Override
	public void cancelGame(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		gameData.setState(GameState.CANCELLED);
		entityManager.get().persist(gameData);
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
