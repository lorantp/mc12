package com.topdesk.mc12.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.Color;

@Data @NoArgsConstructor @EqualsAndHashCode(exclude="game")
@Entity
public final class Move implements DatabaseEntity {
	public static Move createPass(GameData game, Color color) {
		Move move = new Move();
		move.setGame(game);
		move.setColor(color);
		return move;
	}
	
	public static Move create(GameData game, Color color, int x, int y) {
		Move move = createPass(game, color);
		move.setX(x);
		move.setY(y);
		return move;
	}
	
	@Id @GeneratedValue private Long id;
	@ManyToOne(optional=false) private GameData game;
	private Integer x;
	private Integer y;
	private Color color;
	
	@Override
	public String toString() {
		if (isPass()) {
			return String.format("[%s: pass]", color, x, y);
		}
		return String.format("[%s: %d,%d]", color, x, y);
	}
	
	@Transient
	public boolean isPass() {
		return x == null && y == null;
	}
}
