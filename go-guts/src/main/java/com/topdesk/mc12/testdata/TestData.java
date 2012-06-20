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
	@Inject private Provider<EntityManager> entityManager;
	
	private final Player jorn = Player.create("Jorn", "jornh@topdesk.com");
	private final Player bernd = Player.create("Bernd", "berndj@topdesk.com");
	private final Player bart = Player.create("Bart", "barte@topdesk.com");
	private final Player krisz = Player.create("Krisz", "krisztianh@topdesk.com");
	private DateTime nextDate = new DateTime(DateTimeZone.forID("Europe/Berlin")).minusDays(1).withHourOfDay(9);
	private int nextSize = 0;
	
	@Transactional
	public void create() {
		createUsers();
		createGames(jorn, bernd);
		createGames(jorn, bart);
		createGames(jorn, krisz);
		createGames(bernd, bart);
		createGames(bernd, krisz);
		createGames(bart, krisz);
		
		createNewGames(jorn);
		createNewGames(bart);
		createNewGames(bernd);
		createNewGames(krisz);
	}
	
	private void createNewGames(Player player) {
		entityManager.get().persist(new GameData(player, null, nextDate().getMillis(), nextSize(), GameState.INITIATED));
		entityManager.get().persist(new GameData(null, player, nextDate().getMillis(), nextSize(), GameState.INITIATED));
	}
	
	private void createUsers() {
		entityManager.get().persist(bart);
		entityManager.get().persist(bernd);
		entityManager.get().persist(jorn);
		entityManager.get().persist(krisz);
	}
	
	private void createGames(Player black, Player white) {
		createGame(black, white, nextDate(), nextSize(), false);
		createGame(black, white, nextDate(), nextSize(), true);
	}
	
	private BoardSize nextSize() {
		return BoardSize.values()[nextSize++ % 3];
	}
	
	private void createGame(Player black, Player white, DateTime date, BoardSize size, boolean finish) {
		GameData game = new GameData(black, white, date.getMillis(), size, finish ? GameState.FINISHED : GameState.STARTED);
		entityManager.get().persist(game);
		if (finish) {
			createMoves(game, size);
		}
		entityManager.get().flush();
		log.info("Created game: {}", entityManager.get().find(GameData.class, game.getId()));
	}
	
	private DateTime nextDate() {
		return nextDate = nextDate.plusHours(1);
	}
	
	private void createMoves(GameData game, BoardSize size) {
		int moves = 0;
		for (int x = 0; x < size.getSize(); x += 2) {
			for (int y = 1; y < size.getSize(); y += 2) {
				Color color = moves++ % 2 == 0 ? Color.BLACK : Color.WHITE;
				entityManager.get().persist(Move.create(game, color, x, y));
			}
		}
		
		Color color = moves++ % 2 == 0 ? Color.BLACK : Color.WHITE;
		entityManager.get().persist(Move.createPass(game, color));
		
		color = moves++ % 2 == 0 ? Color.BLACK : Color.WHITE;
		entityManager.get().persist(Move.createPass(game, color));
	}
}
