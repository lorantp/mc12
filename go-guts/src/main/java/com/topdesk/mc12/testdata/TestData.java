package com.topdesk.mc12.testdata;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;

@Slf4j
public class TestData {
	private static final boolean ADD_MOVES = false;
	private static final BoardSize SIZE = BoardSize.NINETEEN;
	
	@Inject private Backend backend;
	
	private final Player jorn = new Player(0, "Jorn", "jornh@topdesk.com");
	private final Player bernd = new Player(0, "Bernd", "berndj@topdesk.com");
	private final Player bart = new Player(0, "Bart", "barte@topdesk.com");
	private final Player krisz = new Player(0, "Krisz", "krisztianh@topdesk.com");
	
	public void create() {
		createUsers();
		createGame();
	}
	
	private void createUsers() {
		backend.insert(jorn, bernd, bart, krisz);
	}
	
	private void createGame() {
		Game game = new Game(0, bart, bernd, Collections.<Move>emptyList(), new DateTime().getMillis(), SIZE, 5, 9);
		backend.insert(game);
		if (ADD_MOVES) {
			createMoves(game);
		}
		log.info("Created game: {}", backend.get(Game.class, game.getId()));
	}
	
	private void createMoves(Game game) {
		int moves = 0;
		for (int x = 0; x < SIZE.getSize(); x += 2) {
			for (int y = 1; y < SIZE.getSize(); y += 2) {
				backend.insert(new Move(0, game, x, y, moves++ % 2 == 0 ? bart : bernd));
			}
		}
	}
}
