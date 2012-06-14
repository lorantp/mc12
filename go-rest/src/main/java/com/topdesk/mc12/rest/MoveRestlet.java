package com.topdesk.mc12.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.persistence.Backend;

@Slf4j
@Path("move")
@Consumes(MediaType.APPLICATION_JSON)
public class MoveRestlet {
	@Inject private Backend backend;
	
	@POST
	@Path("/pass")
	public Board pass(Move move, @QueryParam("boardid") long boardId) {
		Board board = backend.get(Board.class, boardId);
		if (move.getX() != null || move.getY() != null) {
			throw new IllegalStateException("Got pass with move coordinate");
		}
		
		Move last = Iterables.getLast(board.getMoves(), null);
		checkTurn(move, last);
		saveMove(move, board);
		return BoardRestlet.fixRecursion(backend.get(Board.class, boardId));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Board move(Move move, @QueryParam("boardid") long boardId) {
		Board board = backend.get(Board.class, boardId);
		
		checkBounds(board.getSize(), move.getX());
		checkBounds(board.getSize(), move.getY());
		
		Move last = null;
		for (Move m : board.getMoves()) {
			if (m.getX() == move.getX() && m.getY() == move.getY()) {
				throw new IllegalStateException("There's already a stone at " + m.getX() + ", " + m.getY());
			}
			last = m;
		}
		
		checkTurn(move, last);
		saveMove(move, board);
		return BoardRestlet.fixRecursion(backend.get(Board.class, boardId));
	}
	
	private void saveMove(Move move, Board board) {
		move.setBoard(board);
		log.info("Adding move {} to {}", move, board);
		backend.insert(move);
	}
	
	private void checkTurn(Move move, Move last) {
		if (last == null) {
			if (move.getColor() != Color.BLACK) {
				throw new IllegalStateException("The first move must be made by the black player");
			}
		}
		else if (last.getColor() == move.getColor()) {
			throw new IllegalStateException("It is not the " + move.getColor() + " player's turn");
		}
	}
	
	private void checkBounds(int size, Integer c) {
		if (c == null) {
			throw new IllegalStateException("Got move without coordinate");
		}
		if (c < 0 || c >= size) {
			throw new IllegalStateException("Move does not fit on board");
		}
	}
}
