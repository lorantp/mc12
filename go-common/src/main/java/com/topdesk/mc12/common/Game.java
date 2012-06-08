package com.topdesk.mc12.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public final class Game {
	private long id;
	private BoardSize size;
	private Player black;
	private Player white;
	
	/**
	 * The amount of white stones captured by the black player
	 */
	private int blackCaptured;
	
	/**
	 * The amount of black stones captured by the white player
	 */
	private int whiteCaptured;
}
