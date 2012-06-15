package com.topdesk.mc12.rules;

import com.topdesk.mc12.persistence.entities.BoardSize;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.entities.Game;


public interface RuleEngine {

	Boolean checkTurn(GameData game, Player player);
	void checkBounds(BoardSize size, Integer coordinate);
	Boolean validPosition(Move move, GameData game);
	Game turnMovesIntoBoard(GameData game);
	
	
//	void checkValidPosition(RestMove move, Game game);
}
