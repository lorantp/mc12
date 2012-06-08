package com.topdesk.mc12.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data @NoArgsConstructor @AllArgsConstructor
public final class Move {
	private long id;
	private int x = -1;
	private int y = -1;
	@NonNull private Color color;
}
