package com.topdesk.mc12.rules;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.rules.entities.Game;
import com.topdesk.mc12.rules.entities.Stone;

public class GoRuleEngine implements RuleEngine {
	@Override
	public Game applyMoves(GameData gameData) {
		Game game = new Game(
				gameData.getId(),
				gameData.getBlack(),
				gameData.getWhite(),
				gameData.getSize(),
				gameData.getMoves().size(),
				gameData.getStart());
		
		for (Move move: gameData.getMoves()) {
			if (move.isPass()) {
				applyPass(game, move.getColor());
			}
			else {
				applyMove(game, move.getColor(), move.getX(), move.getY());
			}
		}
		
		return game;
	}
	
	@Override
	public void applyPass(Game game, Color color) {
		checkTurn(game, color);
		game.applyPass();
	}
	
	@Override
	public void applyMove(Game game, Color color, int x, int y) {
		checkBounds(game.getSize(), x);
		checkBounds(game.getSize(), y);
		checkTurn(game, color);
		checkValidPosition(game, x, y);
		
		game.addStone(x, y, color);
		applyCapture(game);
	}
	
	private void applyCapture(Game game) {
		// NYI
	}
	
	/**
	 * Checks if the intended if a move can be made as intended.
	 * 
	 * A position is invalid if:
	 * <li>the place is already occupied</li>
	 * <li>the move would result in suicide (NYI)</li>
	 * 
	 * @param move is to be checked
	 * @param gameData is the current state of the game.
	 * @throws GoException if the move can't be made for some reason
	 */
	private void checkValidPosition(Game game, int x, int y) {
		for (Stone stone : game.getStones()) {
			if (stone.getX() == x && stone.getY() == y) {
				throw GoException.createNotAcceptable("Board already has a stone at " + x + ", " + y);
			}
		}
	}
	
	/**
	 * Checks if the color of the move corresponds with the expected next move
	 * @param game
	 * @param color
	 * @throws GoException if the colors don't match
	 */
	private void checkTurn(Game game, Color color) {
		if (game.getNextTurn() != color) {
			String nickname = (color == Color.BLACK ? game.getBlack() : game.getWhite()).getNickname();
			throw GoException.createNotAcceptable("It is not " + nickname + "'s turn");
		}
	}
	
	/**
	 * Checks a coordinate to fit the board.
	 * @param size is the size of the board
	 * @param coordinate is to be checked
	 * @throws GoException if it's not the player's turn
	 */
	private void checkBounds(int size, Integer coordinate) {
		if (!(coordinate != null)) {
			throw GoException.createBadRequest("Got move without coordinate");
		}
		if (!(coordinate >= 0 && coordinate < size)) {
			throw GoException.createBadRequest("Move does not fit on board");
		}
	}
}
