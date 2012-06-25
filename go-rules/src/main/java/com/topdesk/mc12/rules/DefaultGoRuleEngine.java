package com.topdesk.mc12.rules;

import java.util.Set;

import javax.inject.Inject;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.Score;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.capturing.CaptureResolver;
import com.topdesk.mc12.rules.entities.Game;
import com.topdesk.mc12.rules.entities.Stone;
import com.topdesk.mc12.rules.scoring.ScoreCalculator;

public class DefaultGoRuleEngine implements GoRuleEngine {
	private static final double KOMI = 5.5;
	
	private final CaptureResolver captureResolver;
	private final ScoreCalculator scoreCalculator;
	
	@Inject
	public DefaultGoRuleEngine(CaptureResolver captureResolver, ScoreCalculator scoreCalculator) {
		this.captureResolver = captureResolver;
		this.scoreCalculator = scoreCalculator;
	}
	
	@Override
	public Game applyMoves(GameData gameData) {
		Game game = new Game(
				gameData.getId(),
				gameData.getBlack(),
				gameData.getWhite(),
				gameData.getBoardSize().getSize(),
				gameData.getStart());
		
		Color lastColor = Color.WHITE; 
		for (Move move: gameData.getMoves()) {
			lastColor = move.getColor();
			if (move.isPass()) {
				applyPass(game, move.getColor());
			}
			else {
				applyMove(game, move.getColor(), move.getX(), move.getY());
			}
		}
		
		if (gameData.getState() == GameState.FINISHED) {			
			Player winner = lastColor == Color.BLACK ? gameData.getWhite() : gameData.getBlack();
			game.setWinner(winner);
		}
		return game;
	}
	
	@Override
	public void applyPass(Game game, Color color) {
		checkTurn(game, color);
		boolean lastMovePass = game.isLastMovePass();
		game.applyPass();
		if (lastMovePass) {
			Score score = scoreCalculator.calculate(game.getStones());
			Player winner = score.getWhite() + KOMI < score.getBlack() ? game.getBlack() : game.getWhite();
			game.setWinner(winner);
		}
	}

	@Override
	public void applySurrender(Game game, Color color) {
		checkTurn(game, color);
		Player winner = color == Color.BLACK ? game.getWhite() : game.getBlack();
		game.setWinner(winner);
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
		
		switch (move.getColor()) {
		case BLACK:
			game.setWhiteStonesCaptured(game.getWhiteStonesCaptured() + capturedStones.size());
			break;
		case WHITE:
			game.setBlackStonesCaptured(game.getBlackStonesCaptured() + capturedStones.size());
			break;
		}
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
			String nickname = (color == Color.BLACK ? game.getBlack() : game.getWhite()).getName();
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
