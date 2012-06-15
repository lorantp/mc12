package com.topdesk.mc12.rules.entities;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;
import com.topdesk.mc12.common.GoException;
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
	private long start;
	
	public Game(long id, Player black, Player white, int size, int totalMoves, long startTime) {
		this.id = id;
		this.black = black;
		this.white = white;
		this.size = size;
		this.totalMoves = totalMoves;
		this.start = startTime;
	}
	
	public void applyMove(Integer x, Integer y, Color color) {
		if (color != nextTurn) {
			throw GoException.createNotAcceptable("Not the " + color + " player's turn");
		}
		stones.add(new Stone(x, y, color));
		nextTurn();
	}
	
	public void applyPass() {
		nextTurn();
	}
	
	private void nextTurn() {
		nextTurn = nextTurn == Color.BLACK ? Color.WHITE : Color.BLACK;
	}
}
