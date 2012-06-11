package com.topdesk.mc12.testdata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.inject.Injector;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;

public class TestData {
	public static void create(Injector injector) {
		TestData testData = new TestData(injector.getInstance(Backend.class));
		testData.createUsers();
		testData.createGame();
	}
	
	private final Backend backend;
	private final Player jorn = new Player(0, "Jorn", "jornh@topdesk.com");
	private final Player bernd = new Player(0, "Bernd", "berndj@topdesk.com");
	private final Player bart = new Player(0, "Bart", "barte@topdesk.com");
	private final Player krisz = new Player(0, "Krisz", "krisztianh@topdesk.com");
	
	private TestData(Backend backend) {
		this.backend = backend;
	}
	
	private void createUsers() {
		backend.insert(jorn, bernd, bart, krisz);
	}
	
	private void createGame() {
		Game game = new Game(0, new Board(0, BoardSize.NINETEEN.getSize(), createMoves()), bart, bernd, 0, 0);
		backend.insert(game);
	}
	
	private ImmutableList<Move> createMoves() {
		Builder<Move> builder = ImmutableList.builder();
		for (int x = 0; x < 19; x += 2) {
			for (int y = 1; y < 19; y += 2) {
				Move move = new Move(0, x, y, Color.values()[Math.random() < 0.5 ? 0 : 1]);
				builder.add(move);
				backend.insert(move);
			}
		}
		return builder.build();
	}
}
