package com.topdesk.mc12.rules.scoring;

import java.util.Set;

import com.topdesk.mc12.common.Score;
import com.topdesk.mc12.rules.entities.Stone;

/**
 * Calculator that takes a Set of stones on the board and calculates a {@link Score} from that.
 */
public interface ScoreCalculator {
	/**
	 * @param stones All the stones that were on the board at the end of the game.
	 */
	Score calculate(Set<Stone> stones);
}
