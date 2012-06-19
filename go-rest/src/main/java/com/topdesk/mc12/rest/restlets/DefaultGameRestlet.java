package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.GameMetaData;
import com.topdesk.mc12.rest.entities.NewGame;
import com.topdesk.mc12.rest.entities.PlayerId;
import com.topdesk.mc12.rest.entities.RestMove;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.entities.Game;

@Slf4j
@Transactional
public class DefaultGameRestlet implements GameRestlet {
	private static final String GAME_METADATA_QUERY = "select game.id, game.start, game.black.nickname, game.white.nickname from GameData game join game.black join game.white";
	
	private static final Function<Object[], GameMetaData> METADATA_FUNCTION = new Function<Object[], GameMetaData>() {
		@Override
		public GameMetaData apply(Object[] input) {
			long id = (Long) input[0];
			long start = (Long) input[1];
			String black = (String) input[2];
			String white = (String) input[3];
			return new GameMetaData(id, start, black, white);
		}
	};
	
	@Inject private Provider<EntityManager> entityManager;
	@Inject private GoRuleEngine ruleEngine;
	
	@Context private SecurityContext context;
	
	@Override
	public Game get(long gameId) {
		log.error("Over here we actually have the elusive and magical {}", context);
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		return ruleEngine.applyMoves(gameData);
	}
	
	@Override
	public List<GameMetaData> getAll() {
		@SuppressWarnings("unchecked")
		List<Object[]> games = entityManager.get().createQuery(GAME_METADATA_QUERY).getResultList();
		return Lists.transform(games, METADATA_FUNCTION);
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
		entityManager.get().persist(Move.createPass(gameData, color));
	}
	
	@Override
	public void move(long gameId, RestMove restMove) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, restMove.getPlayerId());
		ruleEngine.applyMove(game, color, restMove.getX(), restMove.getY());
		
		Move move = Move.create(gameData, color, restMove.getX(), restMove.getY());
		entityManager.get().persist(move);
		log.info("Player {} made move {} in game {}", new Object[] { restMove.getPlayerId(), move, gameData });
	}
	
	@Override
	public long newGame(NewGame newGame) {
		Player player = entityManager.get().find(Player.class, newGame.getPlayerId());
		GameData game = new GameData(null, null, new DateTime().getMillis(), BoardSize.get(newGame.getBoardSize()), GameState.INITIATED);
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
