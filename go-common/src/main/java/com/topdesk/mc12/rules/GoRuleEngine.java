package com.topdesk.mc12.rules;

import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.rules.entities.Game;

/**
 * Implementations of this interface contain all the business rules for a game of Go.
 * It
 */
public interface GoRuleEngine {
	/**
	 * Calculates the state of the board from the {@link GameData}.
	 * @param gameData is the data to calculate from
	 * @return the state of the board
	 * @throws GoException if the rules don't allow the current game state
	 */
	Game applyMoves(GameData gameData);
	
	/**
	 * Apply rules for making a move on an existing game
	 * @param gameData
	 * @param game
	 * @param move
	 * @throws GoException if the rules don't allow this
	 */
	void applyMove(Game game, Color color, int x, int y);
	
	/**
	 * Apply rules for making a pass on an existing game
	 * @param game
	 * @param move
	 * @throws GoException if the rules don't allow this
	 */
	void applyPass(Game game, Color color);
	
}
