package com.topdesk.mc12.rest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GameMetaData {
	private long id;
	private long start;
	private String blackPlayer;
	private String whitePlayer;
}
