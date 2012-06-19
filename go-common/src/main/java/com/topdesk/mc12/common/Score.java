package com.topdesk.mc12.common;

import lombok.Data;

/**
 * Score for the <code>black</code> player and the <code>white</code> player. 
 */
public @Data class Score {
	private final int black;
	private final int white;
}
