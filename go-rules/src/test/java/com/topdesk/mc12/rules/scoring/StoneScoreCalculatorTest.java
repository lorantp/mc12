package com.topdesk.mc12.rules.scoring;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.rules.entities.Stone;

public class StoneScoreCalculatorTest {
	ScoreCalculator calculator = new StoneScoreCalculator();
	
	@Test
	public void testBlackStones() {
		Set<Stone> stones = Sets.newHashSet(new Stone(0, 0, Color.BLACK), new Stone(1, 0, Color.BLACK));
		assertEquals(2, calculator.calculate(stones).getBlack());
		assertEquals(0, calculator.calculate(stones).getWhite());
	}
	
	@Test
	public void testWhiteStones() {
		Set<Stone> stones = Sets.newHashSet(new Stone(0, 0, Color.WHITE), new Stone(1, 0, Color.WHITE));
		assertEquals(0, calculator.calculate(stones).getBlack());
		assertEquals(2, calculator.calculate(stones).getWhite());
	}
	
	@Test
	public void testBothStones() {
		Set<Stone> stones = Sets.newHashSet(new Stone(0, 0, Color.WHITE), new Stone(1, 0, Color.BLACK));
		assertEquals(1, calculator.calculate(stones).getBlack());
		assertEquals(1, calculator.calculate(stones).getWhite());
	}
	
	@Test
	public void testEmpty() {
		Set<Stone> stones = Sets.newHashSet();
		assertEquals(0, calculator.calculate(stones).getBlack());
		assertEquals(0, calculator.calculate(stones).getWhite());
	}
}
