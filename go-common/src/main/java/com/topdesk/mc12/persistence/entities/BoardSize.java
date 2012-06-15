package com.topdesk.mc12.persistence.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access=AccessLevel.PRIVATE)
public enum BoardSize {
	NINE(9),
	THIRTEEN(13),
	NINETEEN(19);
	
	@Getter private final int size;
}
