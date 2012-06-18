package com.topdesk.mc12.rules.capturing;

import java.util.Set;

import com.topdesk.mc12.rules.entities.Stone;

public interface CaptureResolver {
	Set<Stone> calculateCapturedStones(Stone move, Set<Stone> currentStones, int boardSize);
}