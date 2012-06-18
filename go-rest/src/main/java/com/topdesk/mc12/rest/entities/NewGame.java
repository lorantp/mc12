package com.topdesk.mc12.rest.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.Color;

@Data @NoArgsConstructor
public class NewGame {
	private long initiatedPlayerId;
	private int boardSize;
	private Color color;
}
