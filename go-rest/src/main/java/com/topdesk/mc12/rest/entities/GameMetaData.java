package com.topdesk.mc12.rest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.GameState;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameMetaData {
	private long id;
	private long start;
	private GameState state;
	private String blackPlayer;
	private String whitePlayer;
}
