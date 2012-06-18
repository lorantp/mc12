package com.topdesk.mc12.rules;

import java.util.Collections;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.entities.Game;
import com.topdesk.mc12.rules.entities.Stone;

public class DefaultGoRuleEngineTest {
	private final DefaultGoRuleEngine ruleEngine = new DefaultGoRuleEngine(new CaptureResolver() {
		@Override
		public Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize) {
			return Collections.emptySet();
		}
	});
	
	private Game game;
	
	@Before
	public void setupGame() {
		game = new Game(0, new Player(0, "Batman", "magicmissle@thedarkness.com"), new Player(1, "Pikachu", "lightningstrikes@thesameplacetwice.com"), 3, 0, new DateTime().getMillis());
	}

	@Test
	public void noExceptionOnTrivialMoves() {
		ruleEngine.applyMove(game, Color.BLACK, 1, 1);
		ruleEngine.applyMove(game, Color.WHITE, 0, 1);
		ruleEngine.applyMove(game, Color.BLACK, 1, 0);
	}
	
	@Test(expected=GoException.class)
	public void noDoubleMove() {
		ruleEngine.applyMove(game, Color.BLACK, 1, 1);
		ruleEngine.applyMove(game, Color.WHITE, 1, 1);
	}
	
	@Test(expected=GoException.class)
	public void blackStarts() {
		ruleEngine.applyMove(game, Color.WHITE, 1, 1);
	}
	
	@Test(expected=GoException.class)
	public void movesAlternate() {
		ruleEngine.applyMove(game, Color.BLACK, 1, 1);
		ruleEngine.applyMove(game, Color.BLACK, 1, 0);
	}
}