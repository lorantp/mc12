package com.topdesk.mc12.persistence.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.GameState;

@Data @NoArgsConstructor
@Entity
public final class GameData implements DatabaseEntity {
	public static GameData createInitiated(Player player, Color color, long initiate, BoardSize size) {
		GameData gameData = new GameData();
		gameData.setInitiate(initiate);
		gameData.setBoardSize(size);
		gameData.setState(GameState.INITIATED);
		if (color == Color.BLACK) {
			gameData.setBlack(player);
		}
		else {
			gameData.setWhite(player);
		}
		return gameData;
	}
	
	public static GameData createCancelled(Player player, Color color, long initiate, long finish, BoardSize size) {
		GameData gameData = new GameData();
		gameData.setInitiate(initiate);
		gameData.setFinish(finish);
		gameData.setBoardSize(size);
		gameData.setState(GameState.INITIATED);
		if (color == Color.BLACK) {
			gameData.setBlack(player);
		}
		else {
			gameData.setWhite(player);
		}
		return gameData;
	}
	
	public static GameData createStarted(Player black, Player white, long initiate, long start, BoardSize size) {
		GameData gameData = new GameData();
		gameData.setState(GameState.STARTED);
		gameData.setInitiate(initiate);
		gameData.setStart(start);
		gameData.setBoardSize(size);
		gameData.setBlack(black);
		gameData.setWhite(white);
		return gameData;
	}
	
	public static GameData createFinished(Player black, Player white, long initiate, long start, long finish, BoardSize size) {
		GameData gameData = new GameData();
		gameData.setState(GameState.FINISHED);
		gameData.setInitiate(initiate);
		gameData.setStart(start);
		gameData.setFinish(finish);
		gameData.setBoardSize(size);
		gameData.setBlack(black);
		gameData.setWhite(white);
		return gameData;
	}
	
	@Id @GeneratedValue private Long id;
	@ManyToOne() private Player black;
	@ManyToOne() private Player white;
	@OrderBy("id") @OneToMany(mappedBy="game") private List<Move> moves;
	private long initiate = -1;
	private Long start;
	private Long finish;
	private BoardSize boardSize;
	private GameState state;
}
