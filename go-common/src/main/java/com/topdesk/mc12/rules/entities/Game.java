package com.topdesk.mc12.rules.entities;

import static com.google.common.base.Preconditions.*;

import java.util.Collection;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import com.google.common.collect.Sets;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.persistence.entities.Player;

@Data
public class Game {
	private final long id;
	private final Player black;
	private final Player white;
	private final int size;
	private final long start;
	
	@Setter(AccessLevel.NONE) private int totalMoves = 0;
	
	private final Set<Stone> stones = Sets.newHashSet();
	@Setter(AccessLevel.NONE) private int blackStonesCaptured = 0;
	@Setter(AccessLevel.NONE) private int whiteStonesCaptured = 0;
	private Color nextTurn = Color.BLACK;
	@Setter(AccessLevel.NONE) private boolean lastMovePass = false;
	private boolean finished = false;
	
	public Game(long id, Player black, Player white, int size, long start) {
		this.id = id;
		this.black = black;
		this.white = white;
		this.size = size;
		this.start = start;
	}
	
	public void addStone(int x, int y, Color color) {
		stones.add(new Stone(x, y, color));
		nextTurn();
		lastMovePass = false;
	}
	
	public void applyPass() {
		nextTurn();
		lastMovePass = true;
	}
	
	public void capture(Collection<Stone> captured, Color color) {
		checkArgument(stones.containsAll(captured), "Some captured stones are not on this board");
		stones.removeAll(captured);
		
		switch (color) {
		case BLACK:
			whiteStonesCaptured += captured.size();
			break;
		case WHITE:
			blackStonesCaptured += captured.size();
			break;
		}
	}
	
	private void nextTurn() {
		nextTurn = nextTurn == Color.BLACK ? Color.WHITE : Color.BLACK;
		totalMoves++;
	}
}
