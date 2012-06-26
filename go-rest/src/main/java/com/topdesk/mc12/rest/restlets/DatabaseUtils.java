package com.topdesk.mc12.rest.restlets;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.rules.entities.Game;

@Slf4j
@Singleton
public class DatabaseUtils {
	@Inject private Provider<EntityManager> entityManager;
	
	@Transactional
	public Game update(GameData gameData, Game game) {
		if (gameData.getWinner() == null && gameData.getState() == GameState.FINISHED) {
			gameData.setWinner(game.getWinner());
			log.warn("Upgrading winner field for game {}, winner = {}", gameData.getId(), game.getWinner());
			entityManager.get().merge(gameData);
			entityManager.get().flush();
		}
		return game;
	}
}
