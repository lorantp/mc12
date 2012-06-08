package com.topdesk.mc12.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public final class Board {
	private long id;
	private long gameid;
	private List<Move> moves;
}
