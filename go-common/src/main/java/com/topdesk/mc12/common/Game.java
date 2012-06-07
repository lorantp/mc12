package com.topdesk.mc12.common;

import lombok.Data;

@Data
public final class Game {
	private final long id;
	private final Player black;
	private final Player white;
}
