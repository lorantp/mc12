package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;
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
@RequestScoped
public class DefaultGameRestlet implements GameRestlet {
	private final Provider<EntityManager> entityManager;
	private final GoRuleEngine ruleEngine;
	private final Player player;
	
	@Inject
	public DefaultGameRestlet(Provider<EntityManager> entityManager, GoRuleEngine ruleEngine, Player player) {
		this.entityManager = entityManager;
		this.ruleEngine = ruleEngine;
		this.player = player;
	}
	
	@Override
	public Game get(long gameId) {
		log.error("Over here we actually have the elusive and magical {}", player);
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		if (gameData == null) {
			throw GoException.createNotFound("Game with id " + gameId + " not found");
		}
		return ruleEngine.applyMoves(gameData);
	}
	
	@Override
	public List<GameMetaData> getAll() {
		return getAll(null);
	}
	
	@Override
	public List<GameMetaData> getAll(GameState state) {
		CriteriaQuery<GameMetaData> query = entityManager.get().getCriteriaBuilder().createQuery(GameMetaData.class);
		Root<GameData> game = query.from(GameData.class);
		query.multiselect(
				game.get("id"),
				game.get("start"),
				game.get("state"),
				game.join("black", JoinType.LEFT).get("nickname"),
				game.join("white", JoinType.LEFT).get("nickname"));
		if (state != null) {
			query.where(game.get("state").in(state));
		}
		return entityManager.get().createQuery(query).getResultList();
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
		
		entityManager.get().persist(Move.createPass(gameData, color));
		entityManager.get().flush();
		log.info("Player {} passed in game {}", playerId.getPlayerId(), game);
	}
	
	@Override
	public void move(long gameId, RestMove restMove) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData, restMove.getPlayerId());
		ruleEngine.applyMove(game, color, restMove.getX(), restMove.getY());
		
		Move move = Move.create(gameData, color, restMove.getX(), restMove.getY());
		entityManager.get().persist(move);
		entityManager.get().flush();
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
		entityManager.get().flush();
		log.info("Player {} initiated game {}", player, game);
		return game.getId();
	}
	
	@Override
	public void startGame(long gameId, PlayerId playerId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		
		long playerIdValue = playerId.getPlayerId();
		Player joiningPlayer = entityManager.get().find(Player.class, playerIdValue);
		if (joiningPlayer == null) {
			throw GoException.createNotFound("Player with id of " + playerId + "is not recogized");
		}
		
		boolean whiteInitiated = gameData.getBlack() == null;
		Player initiated = whiteInitiated ? gameData.getWhite() : gameData.getBlack();
		if (playerIdValue == initiated.getId()) {
			throw GoException.createNotAcceptable("Can't play against yourself");
		}
		
		Player player = entityManager.get().find(Player.class, playerId.getPlayerId());
		if (player == null) {
			throw GoException.createNotFound("Player with id " + playerId.getPlayerId() + " not found");
		}
		
		if (gameData.getBlack() == null) {
			gameData.setBlack(player);
		}
		else {
			gameData.setWhite(player);
		}
		gameData.setState(GameState.STARTED);
		entityManager.get().merge(gameData);
		entityManager.get().flush();
		log.info("Player {} joined and started game {}", player, gameData);
	}
	
	@Override
	public void cancelGame(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		gameData.setState(GameState.CANCELLED);
		entityManager.get().persist(gameData);
		entityManager.get().flush();
		log.info("Someone cancelled game {}", gameData);
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
