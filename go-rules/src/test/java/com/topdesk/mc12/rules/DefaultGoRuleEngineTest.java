package com.topdesk.mc12.rules;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.Score;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.capturing.CaptureResolver;
import com.topdesk.mc12.rules.entities.Game;
import com.topdesk.mc12.rules.entities.Stone;
import com.topdesk.mc12.rules.scoring.ScoreCalculator;

public class DefaultGoRuleEngineTest {
	private static final Player PLAYER_BLACK = Player.create("Batman", "magicmissle@thedarkness.com");
	private static final Player PLAYER_WHITE = Player.create("Pikachu", "lightningstrikes@thesameplacetwice.com");
	
	private static final CaptureResolver CAPTURE_RESOLVER = new CaptureResolver() {
		@Override
		public Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize) {
			return Collections.emptySet();
		}
	};
	
	private GoRuleEngine ruleEngine;
	private Game game;
	private MockScoreCalculator scoreCalculator;
	
	@Before
	public void setupGame() {
		game = new Game(0, PLAYER_BLACK, PLAYER_WHITE, 3, new DateTime().getMillis());
		scoreCalculator = new MockScoreCalculator();
		ruleEngine = new DefaultGoRuleEngine(CAPTURE_RESOLVER, scoreCalculator);
	}
	
	@Test
	public void playerCanPass() {
		int totalMoves = game.getTotalMoves();
		ruleEngine.applyPass(game, Color.BLACK);
		ruleEngine.applyMove(game, Color.WHITE, 0, 0);
		assertThat(totalMoves + 2, is(game.getTotalMoves()));
	}
	
	@Test
	public void blackCanSurrender() {
		ruleEngine.applySurrender(game, Color.BLACK);
		assertThat(game.getWinner(), is(PLAYER_WHITE));
	}
	
	@Test
	public void whiteCanSurrender() {
		ruleEngine.applyPass(game, Color.BLACK);
		ruleEngine.applySurrender(game, Color.WHITE);
		assertThat(game.getWinner(), is(PLAYER_BLACK));
	}
	
	@Test
	public void whiteWinsWithDifferenceOf_5() {
		scoreCalculator.setBlackScore(5);
		scoreCalculator.setWhiteScore(0);
		ruleEngine.applyPass(game, Color.BLACK);
		ruleEngine.applyPass(game, Color.WHITE);
		assertThat(game.getWinner(), is(PLAYER_WHITE));
	}
	
	@Test
	public void blackWinsWithDifferenceOf_6() {
		scoreCalculator.setBlackScore(6);
		scoreCalculator.setWhiteScore(0);
		ruleEngine.applyPass(game, Color.BLACK);
		ruleEngine.applyPass(game, Color.WHITE);
		assertThat(game.getWinner(), is(PLAYER_BLACK));
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
	
	private static class MockScoreCalculator implements ScoreCalculator {
		private int black = 0;
		private int white = 0;
		
		private void setBlackScore(int black) {
			this.black = black;
		}

		private void setWhiteScore(int white) {
			this.white = white;
		}

		@Override
		public Score calculate(Set<Stone> stones) {
			return new Score(black, white);
		}
	}
}