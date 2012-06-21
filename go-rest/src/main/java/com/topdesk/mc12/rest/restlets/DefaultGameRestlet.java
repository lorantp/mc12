package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
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
		CriteriaBuilder builder = entityManager.get().getCriteriaBuilder();
		CriteriaQuery<GameMetaData> query = builder.createQuery(GameMetaData.class);
		Root<GameData> game = query.from(GameData.class);
		Path<Object> stateField = game.get("state");
		query.multiselect(
				game.get("id"),
				game.get("start"),
				stateField,
				game.join("black", JoinType.LEFT).get("name"),
				game.join("white", JoinType.LEFT).get("name"));
		
		if (state != null) {
			query.where(stateField.in(state));
		}
		else {
			query.where(stateField.in(GameState.INITIATED, GameState.STARTED, GameState.FINISHED));
			query.orderBy(builder.asc(stateField), builder.desc(game.get("start")));
		}
		return entityManager.get().createQuery(query).getResultList();
	}
	
	@Override
	public void pass(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData);
		ruleEngine.applyPass(game, color);
		
		if (game.isFinished()) {
			gameData.setState(GameState.FINISHED);
			entityManager.get().merge(gameData);
		}
		
		entityManager.get().persist(Move.createPass(gameData, color));
		entityManager.get().flush();
		log.info("Player {} passed in game {}", player.getName(), game);
	}
	
	@Override
	public void move(long gameId, RestMove restMove) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		Game game = ruleEngine.applyMoves(gameData);
		Color color = getPlayerColor(gameData);
		ruleEngine.applyMove(game, color, restMove.getX(), restMove.getY());
		
		Move move = Move.create(gameData, color, restMove.getX(), restMove.getY());
		entityManager.get().persist(move);
		entityManager.get().flush();
		log.info("Player {} made move {} in game {}", new Object[] { player.getName(), move, gameData.getId() });
	}
	
	@Override
	public long newGame(NewGame newGame) {
		GameData game = new GameData(null, null, new DateTime().getMillis(), BoardSize.get(newGame.getBoardSize()), GameState.INITIATED);
		if (newGame.getColor() == Color.BLACK) {
			game.setBlack(player);
		}
		else {
			game.setWhite(player);
		}
		
		entityManager.get().persist(game);
		entityManager.get().flush();
		log.info("Player {} initiated game {}", player.getName(), game.getId());
		return game.getId();
	}
	
	@Override
	public void startGame(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		
		boolean whiteInitiated = gameData.getBlack() == null;
		Player initiated = whiteInitiated ? gameData.getWhite() : gameData.getBlack();
		if (player.getId() == initiated.getId()) {
			throw GoException.createNotAcceptable("Can't play against yourself");
		}
		
		if (gameData.getBlack() == null) {
			gameData.setBlack(player);
		}
		else if (gameData.getWhite() == null) {
			gameData.setWhite(player);
		}
		else {
			throw new IllegalStateException("Inconsistent game state");
		}
		
		gameData.setState(GameState.STARTED);
		entityManager.get().merge(gameData);
		entityManager.get().flush();
		log.info("Player {} joined and started game {}", player.getName(), gameData.getId());
	}
	
	@Override
	public void cancelGame(long gameId) {
		GameData gameData = entityManager.get().find(GameData.class, gameId);
		checkInitiated(gameData);
		getPlayerColor(gameData);
		gameData.setState(GameState.CANCELLED);
		entityManager.get().persist(gameData);
		entityManager.get().flush();
		log.info("Player {} cancelled game {}", player.getName(), gameData);
	}
	
	private void checkInitiated(GameData gameData) {
		if (gameData.getState() != GameState.INITIATED) {
			throw GoException.createNotAcceptable("Game is not in INITIATED state");
		}
	}
	
	private Color getPlayerColor(GameData game) {
		if (game.getBlack() != null && game.getBlack().getId() == player.getId()) {
			return Color.BLACK;
		}
		else if (game.getWhite() != null && game.getWhite().getId() == player.getId()) {
			return Color.WHITE;
		}
		throw GoException.createUnauthorized("Player " + player.getName() + " does not participate in game " + game.getId());
	}
}
