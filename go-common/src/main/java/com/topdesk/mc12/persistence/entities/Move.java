package com.topdesk.mc12.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.Color;

@Data @NoArgsConstructor @EqualsAndHashCode(exclude="game")
@Entity
public final class Move implements DatabaseEntity {
	public static Move createSurrender(GameData game, Color color) {
		Move move = new Move();
		move.setType(MoveType.SURRENDER);
		move.setGame(game);
		move.setColor(color);
		return move;
	}
	
	public static Move createPass(GameData game, Color color) {
		Move move = new Move();
		move.setType(MoveType.PASS);
		move.setGame(game);
		move.setColor(color);
		return move;
	}
	
	public static Move create(GameData game, Color color, int x, int y) {
		Move move = createPass(game, color);
		move.setType(MoveType.MOVE);
		move.setX(x);
		move.setY(y);
		return move;
	}
	
	@Id @GeneratedValue private Long id;
	@ManyToOne(optional=false) private GameData game;
	@Column(nullable=false) private MoveType type;
	@Column(nullable=false, updatable=false) private Color color;
	@Column(updatable=false) private Integer x;
	@Column(updatable=false) private Integer y;
	
	@Override
	public String toString() {
		if (type == MoveType.MOVE) {
			return String.format("[%s %s: %d,%d]", color, type, x, y);
		}
		return String.format("[%s %s]", color, type);
	}
}
