package com.topdesk.mc12.rules.entities;

import java.util.Collection;
import java.util.List;

import lombok.Data;

import com.google.common.collect.Lists;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.persistence.entities.Player;

@Data
public class Game {
	private final long id;
	private final Player black;
	private final Player white;
	private final int size;
	private final int totalMoves;
	private final long start;
	
	private final List<Stone> stones = Lists.newArrayList();
	private int blackCaptured = 0;
	private int whiteCaptured = 0;
	private Color nextTurn = Color.BLACK;
	
	public Game(long id, Player black, Player white, int size, int totalMoves, long start) {
		this.id = id;
		this.black = black;
		this.white = white;
		this.size = size;
		this.totalMoves = totalMoves;
		this.start = start;
	}
	
	public void addStone(int x, int y, Color color) {
		stones.add(new Stone(x, y, color));
		nextTurn();
	}
	
	public void removeStones(Collection<Stone> toRemove) {
		stones.removeAll(toRemove);
	}
	
	public void applyPass() {
		nextTurn();
	}
	
	public void capture(int amount, Color color) {
		switch (color) {
		case BLACK:
			whiteCaptured += amount;
			break;
		case WHITE:
			blackCaptured += amount;
			break;
		}
	}
	
	private void nextTurn() {
		nextTurn = nextTurn == Color.BLACK ? Color.WHITE : Color.BLACK;
	}
}
