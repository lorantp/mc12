package com.topdesk.mc12.rules.scoring;

import java.util.Set;

import com.topdesk.mc12.common.Score;
import com.topdesk.mc12.rules.entities.Stone;

public interface ScoreCalculator {
	Score calculate(Set<Stone> stones);
}
