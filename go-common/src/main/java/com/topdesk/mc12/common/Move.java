package com.topdesk.mc12.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Move {
	@Id @GeneratedValue private long id;
	@ManyToOne(optional=false) private Board board;
	private int x = -1;
	private int y = -1;
	@Column(nullable=false) private Color color;
	
	@Override
	public String toString() {
		return String.format("[%s: %d,%d]", color, x, y);
	}
}
