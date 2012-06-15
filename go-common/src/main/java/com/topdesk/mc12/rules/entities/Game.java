package com.topdesk.mc12.rules.entities;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;
import com.topdesk.mc12.persistence.entities.Color;
import com.topdesk.mc12.persistence.entities.Player;

@Data @NoArgsConstructor
public class Game {
	private long id;
	private Player black;
	private Player white;
	private int size;
	private int totalMoves;
	private List<Stone> stones = Lists.newArrayList();
	private int blackCaptured = 0;
	private int whiteCaptured = 0;
	private Color nextTurn = Color.BLACK;
	
	public Game(long id, Player black, Player white, int size, int totalMoves) {
		this.id = id;
		this.black = black;
		this.white = white;
		this.size = size;
		this.totalMoves = totalMoves;
	}
	
	public void applyMove(Integer x, Integer y, Color color) {
		stones.add(new Stone(x, y, color));
		nextTurn();
	}
	
	private void nextTurn() {
		nextTurn = nextTurn == Color.BLACK ? Color.WHITE : Color.BLACK;
	}
}
