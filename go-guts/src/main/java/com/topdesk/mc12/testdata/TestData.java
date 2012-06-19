package com.topdesk.mc12.testdata;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
public class TestData {
	private static final boolean ADD_MOVES = false;
	private static final BoardSize SIZE = BoardSize.NINETEEN;

	@Inject private Provider<EntityManager> entityManager;
	
	private final Player jorn = new Player(0, "Jorn", "jornh@topdesk.com");
	private final Player bernd = new Player(0, "Bernd", "berndj@topdesk.com");
	private final Player bart = new Player(0, "Bart", "barte@topdesk.com");
	private final Player krisz = new Player(0, "Krisz", "krisztianh@topdesk.com");
	
	@Transactional
	public void create() {
		createUsers();
		createGame();
	}
	
	private void createUsers() {
		entityManager.get().persist(bart);
		entityManager.get().persist(bernd);
		entityManager.get().persist(jorn);
		entityManager.get().persist(krisz);
	}
	
	private void createGame() {
		DateTime start = new DateTime(DateTimeZone.forID("Europe/Berlin")).withZoneRetainFields(DateTimeZone.UTC);
		GameData game = new GameData(0, bart, bernd, null, start.getMillis(), SIZE, GameState.STARTED);
		entityManager.get().persist(game);
		if (ADD_MOVES) {
			createMoves(game);
		}
		entityManager.get().flush();
		log.info("Created game: {}", entityManager.get().find(GameData.class, game.getId()));
	}
	
	private void createMoves(GameData game) {
		int moves = 0;
		for (int x = 0; x < SIZE.getSize(); x += 2) {
			for (int y = 1; y < SIZE.getSize(); y += 2) {
				Color color = moves++ % 2 == 0 ? Color.BLACK : Color.WHITE;
				if (Math.random() < 0.1) {
					entityManager.get().persist(new Move(0, game, null, null, color));
				}
				else {
					entityManager.get().persist(new Move(0, game, x, y, color));
				}
			}
		}
	}
}
