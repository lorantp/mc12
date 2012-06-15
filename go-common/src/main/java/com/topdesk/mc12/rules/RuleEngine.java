package com.topdesk.mc12.rules;

import com.topdesk.mc12.persistence.entities.BoardSize;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.entities.Game;


public interface RuleEngine {

	/**
	 * Checks if it's the players turn.
	 * @param gameData is the game data
	 * @param player is to be checked
	 * @throws IllegalStateException if it's not the player's turn
	 */
	void checkTurn(GameData gameData, Player player);
	
	/**
	 * Checks a coordinate to fit the board.
	 * @param boardSize is the size of the board
	 * @param coordinate is to be checked
	 * @throws IllegalStateException if it's not the player's turn
	 */
	void checkBounds(BoardSize boardSize, Integer coordinate);
	
	/**
	 * Checks if the intended if a move can be made as intended.
	 * 
	 * A position is invalid if:
	 * <li>the place is already occupied</li>
	 * <li>the move would result in suicide</li>
	 * 
	 * @param move is to be checked
	 * @param gameData is the current state of the game.
	 * @throws IllegalStateException if the move can't be made for some reason
	 */
	void checkValidPosition(Move move, GameData gameData);
	
	/**
	 * Calculates the state of the board from the {@link GameData}.
	 * @param gameData is the data to calculate from
	 * @return the state of the board
	 */
	Game turnMovesIntoBoard(GameData gameData);
	
}
