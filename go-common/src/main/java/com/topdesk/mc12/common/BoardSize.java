package com.topdesk.mc12.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access=AccessLevel.PRIVATE)
public enum BoardSize {
	NINE(9),
	THIRTEEN(13),
	NINETEEN(19);
	
	@Getter private final int size;
	
	public static BoardSize get(int boardSize) {
		switch (boardSize) {
		case 9: return NINE;
		case 13: return THIRTEEN;
		case 19: return NINETEEN;
		default: throw GoException.createBadRequest("No board size " + boardSize);
		}
	}
}
