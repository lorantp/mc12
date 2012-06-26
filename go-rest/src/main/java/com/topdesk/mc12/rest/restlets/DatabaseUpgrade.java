package com.topdesk.mc12.rest.restlets;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.MoveType;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.entities.Game;

@Slf4j
@Singleton
public class DatabaseUpgrade {
	@Inject private GoRuleEngine ruleEngine;
	@Inject private Provider<EntityManager> entityManager;
	
	/**
	 * Ensures all games have consistent finished state by adding the 'surrendered' move if needed, and setting the winner field.
	 */
	@Transactional
	public void version0001() {
		ensureMoveTypes();
		ensureFinishingMove();
		ensureWinner();
	}
	
	private void ensureMoveTypes() {
		for (GameData game : queryAllGames(null)) {
			for (Move move : game.getMoves()) {
				if (move.getType() == null) {
					setMoveType(move);
				}
			}
			entityManager.get().flush();
		}
	}
	
	private void setMoveType(Move move) {
		if (move.getX() != null && move.getY() != null) {
			log.debug("Upgrading move {} to type MOVE", move);
			move.setType(MoveType.MOVE);
		}
		else {
			log.debug("Upgrading move {} to type PASS", move);
			move.setType(MoveType.PASS);
		}
		entityManager.get().merge(move);
	}
	
	private void ensureFinishingMove() {
		for (GameData game : queryAllGames(GameState.FINISHED)) {
			if (game.getMoves().size() >= 2) {
				Iterator<Move> moves = Lists.reverse(game.getMoves()).iterator();
				if (moves.next().getType() == MoveType.PASS
						&& moves.next().getType() == MoveType.PASS) {
					log.debug("Game {} finished by two passes", game.getId());
					continue;
				}
			}
			
			// for finished games, if the last two moves aren't both passes,
			// it must have been finished by surrendering
			if (game.getMoves().size() % 2 == 0) {
				log.debug("Added surrendering move by {} player to game {}", Color.BLACK, game.getId());
				entityManager.get().persist(Move.createSurrender(game, Color.BLACK));
			}
			else {
				log.debug("Added surrendering move by {} player to game {}", Color.WHITE, game.getId());
				entityManager.get().persist(Move.createSurrender(game, Color.WHITE));
			}
		}
		entityManager.get().flush();
	}
	
	private void ensureWinner() {
		// query all games to ensure state is correct
		for (GameData gameData : queryAllGames(null)) {
			try {
				Game game = ruleEngine.applyMoves(gameData);
				
				if (gameData.getState() == GameState.FINISHED && gameData.getWinner() == null) {
					gameData.setWinner(game.getWinner());
					entityManager.get().merge(gameData);
				}
			}
			catch (RuntimeException e) {
				log.error("State for game {} not correct after upgrading", gameData.getId(), e);
			}
		}
		entityManager.get().flush();
	}
	
	private List<GameData> queryAllGames(GameState state) {
		CriteriaBuilder builder = entityManager.get().getCriteriaBuilder();
		CriteriaQuery<GameData> query = builder.createQuery(GameData.class);
		Root<GameData> root = query.from(GameData.class);
		query.select(root);
		if (state != null) {
			query.where(builder.equal(root.get("state"), state));
		}
		return entityManager.get().createQuery(query).getResultList();
	}
}
