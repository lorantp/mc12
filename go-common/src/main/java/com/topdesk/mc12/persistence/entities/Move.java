package com.topdesk.mc12.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.topdesk.mc12.common.Color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(exclude="game")
@Entity
public final class Move implements DatabaseEntity {
	@Id @GeneratedValue private long id;
	@ManyToOne(optional=false) private GameData game;
	private Integer x;
	private Integer y;
	private Color color;
	
	@Override
	public String toString() {
		if (isPass()) {
		}
		return String.format("[%s: %d,%d]", color, x, y);
	}
	
	@Transient
	public boolean isPass() {
		return x == null && y == null;
	}
}
