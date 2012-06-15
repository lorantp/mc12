package com.topdesk.mc12.rules;

import static com.google.common.base.Preconditions.checkState;

import javax.inject.Inject;

import com.google.common.base.Objects;
import com.topdesk.mc12.persistence.entities.BoardSize;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rules.entities.Game;

public class DefaultGoRuleEngine implements GoRuleEngine {
	private final CaptureResolver captureResolver;
	
	@Inject
	public DefaultGoRuleEngine(CaptureResolver captureResolver) {
		this.captureResolver = captureResolver;
	}
	
	@Override
	public void checkTurn(GameData gameData, Player player) {
		Player expectedPlayer = gameData.getMoves().size() % 2 == 0 ? 
				gameData.getBlack(): 
				gameData.getWhite(); 
		if (!Objects.equal(expectedPlayer, player)) {			
			throw new IllegalStateException("It's not " + player.getNickname() + "'s turn");
		}
	}
	
	@Override
	public void checkBounds(BoardSize boardSize, Integer coordinate) {
		checkState(coordinate != null, "Got move without coordinate");
		checkState(coordinate >= 0 && coordinate < boardSize.getSize(), "Move does not fit on board");
	}
	
	@Override
	public void checkValidPosition(Move move, GameData gameData) {
		if(move.isPass()) {
			return;
		}
	}
	
	@Override
	public Game turnMovesIntoBoard(GameData gameData) {
		Game game = new Game(
				gameData.getId(),
				gameData.getBlack(),
				gameData.getWhite(), 
				gameData.getSize(), 
				gameData.getMoves().size(),
				gameData.getStart());
		
		for(Move move: gameData.getMoves()) {
			if (!move.isPass()) {
				game.applyMove(move.getX(), move.getY(), move.getColor());
			}
		}
		
		return game;
	}
}
