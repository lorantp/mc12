package com.topdesk.mc12.rules.entities;

import com.topdesk.mc12.common.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Stone {
	private int x;
	private int y;
	private Color color;
}
