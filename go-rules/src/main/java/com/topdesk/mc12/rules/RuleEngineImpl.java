package com.topdesk.mc12.rules;

import com.topdesk.mc12.persistence.entities.BoardSize;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.entities.Game;

public class RuleEngineImpl implements RuleEngine {

	public Boolean varifyMove(Move move, long gameId) {
		
		return false;
		
	}
	
	@Override
	public Boolean checkTurn(GameData game, Player player) {
		if (game.getMoves().size() % 2 == 0) {
			if (!player.equals(game.getBlack())) {
				throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
			}
		}
		else if (!player.equals(game.getWhite())) {
			throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
		}
		return false;
	}

	@Override
	public void checkBounds(BoardSize size, Integer coordinate) {
		if (coordinate == null) {
			throw new IllegalStateException("Got move without coordinate");
		}
		if (coordinate < 0 || coordinate >= size.getSize()) {
			throw new IllegalStateException("Move does not fit on board");
		}
		
	}

	@Override
	public Boolean validPosition(Move move, GameData game) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game turnMovesIntoBoard(GameData game) {
		Game board = new Game(game.getBlack(), game.getWhite(), game.getSize(), game.getMoves().size());
		
		for(Move move: game.getMoves()) {
			if (!move.isPass()) {
				board.applyMove(move.getX(), move.getY(), move.getColor());
			}
		}
		
		return board;
	}
}
