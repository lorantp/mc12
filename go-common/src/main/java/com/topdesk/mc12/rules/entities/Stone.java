package com.topdesk.mc12.rules.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.Color;

@Data @AllArgsConstructor @NoArgsConstructor
public class Stone {
	private int x;
	private int y;
	private Color color;
	
	@Override
	public String toString() {
		return String.format("[%s: %d,%d]", color, x, y);
	}
}
