package com.topdesk.mc12.rules.scoring;

import java.util.Set;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Score;
import com.topdesk.mc12.rules.entities.Stone;

public class StoneScoreCalculator implements ScoreCalculator {
	@Override
	public Score calculate(Set<Stone> stones) {
		int black = 0;
		int white = 0;
		for (Stone stone : stones) {
			if(stone.getColor() == Color.BLACK) {
				black++;
			} else {
				white++;
			}
		}
		return new Score(black, white);
	}
}
