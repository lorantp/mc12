package com.topdesk.mc12.rules;

import static com.google.common.base.Preconditions.*;

import java.util.Set;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.rules.entities.Game;
import com.topdesk.mc12.rules.entities.Stone;

@Slf4j
public class DefaultGoRuleEngine implements GoRuleEngine {
	private final CaptureResolver captureResolver;
	
	@Inject
	public DefaultGoRuleEngine(CaptureResolver captureResolver) {
		this.captureResolver = captureResolver;
	}
	
	@Override
	public Game applyMoves(GameData gameData) {
		if (gameData.getState() != GameState.STARTED) {
			log.warn("Creating Game object for already finished game - this may or may not be correct");
		}
		Game game = new Game(
				gameData.getId(),
				gameData.getBlack(),
				gameData.getWhite(),
				gameData.getSize(),
				gameData.getStart());
		
		for (Move move: gameData.getMoves()) {
			if (move.isPass()) {
				applyPass(game, move.getColor());
			}
			else {
				applyMove(game, move.getColor(), move.getX(), move.getY());
			}
		}
		
		checkState(game.isFinished() == (gameData.getState() == GameState.FINISHED), "Inconsistent game state");
		
		return game;
	}
	
	@Override
	public void applyPass(Game game, Color color) {
		checkTurn(game, color);
		boolean lastMovePass = game.isLastMovePass();
		game.applyPass();
		if (lastMovePass) {
			game.setFinished(true);
		}
	}
	
	@Override
	public void applyMove(Game game, Color color, int x, int y) {
		checkBounds(game.getSize(), x);
		checkBounds(game.getSize(), y);
		checkTurn(game, color);
		checkValidPosition(game, x, y);
		
		applyCapture(new Stone(x, y, color), game);
		game.addStone(x, y, color);
	}
	
	private void applyCapture(Stone move, Game game) {
		Set<Stone> capturedStones = captureResolver.calculateCapturedStones(move, game.getStones(), game.getSize());
		game.capture(capturedStones, move.getColor());
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
		if (game.isFinished()) {
			throw GoException.createNotAcceptable("This game is already finished");
		}
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
