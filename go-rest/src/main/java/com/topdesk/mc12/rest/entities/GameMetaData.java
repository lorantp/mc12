package com.topdesk.mc12.rest.entities;

import lombok.Data;

import com.topdesk.mc12.common.GameState;

@Data
public class GameMetaData {
	private final long id;
	private final long initiate;
	private final long start;
	private final long finish;
	private final GameState state;
	private final int boardSize;
	private final String blackPlayer;
	private final String whitePlayer;
	private final String winner;
}
