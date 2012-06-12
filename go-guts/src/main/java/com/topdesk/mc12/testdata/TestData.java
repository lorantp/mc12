package com.topdesk.mc12.testdata;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;

@Slf4j
public class TestData {
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
		Board board = new Board(0, BoardSize.NINETEEN.getSize(), Collections.<Move>emptyList());
		Game game = new Game(0, board, bart, bernd, new DateTime().getMillis(), 5, 9);
		backend.insert(board, game);
		createMoves(board);
		log.info("Created game: {}", backend.get(Game.class, game.getId()));
	}
	
	private void createMoves(Board board) {
		for (int x = 0; x < 19; x += 2) {
			for (int y = 1; y < 19; y += 2) {
				Move move = new Move(0, board, x, y, getRandomColor());
				backend.insert(move);
			}
		}
	}
	
	private Color getRandomColor() {
		return Color.values()[Math.random() < 0.5 ? 0 : 1];
	}
}