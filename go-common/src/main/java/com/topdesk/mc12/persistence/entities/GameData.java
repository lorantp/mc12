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
import com.topdesk.mc12.common.GameState;

@Data @NoArgsConstructor
@Entity
public final class GameData implements DatabaseEntity {
	@Id @GeneratedValue private Long id;
	@ManyToOne() private Player black;
	@ManyToOne() private Player white;
	@OrderBy("id") @OneToMany(mappedBy="game") private List<Move> moves;
	private long start;
	private BoardSize boardSize;
	private GameState state;
	
	public GameData(Player black, Player white, long start, BoardSize boardSize, GameState state) {
		this.black = black;
		this.white = white;
		this.start = start;
		this.boardSize = boardSize;
		this.state = state;
	}
}
