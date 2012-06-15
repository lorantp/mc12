package com.topdesk.mc12.rules;

import java.util.Set;

import com.topdesk.mc12.rules.entities.Stone;

public interface CaptureResolver {

	public abstract Set<Stone> calculateCapturedStones(Stone move,
			Set<Stone> currentStones, int boardSize);

}