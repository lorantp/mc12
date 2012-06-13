package com.topdesk.mc12.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(exclude="board")
@Entity
public final class Move {
	@Id @GeneratedValue private long id;
	@ManyToOne(optional=false) private Board board;
	private Integer x = -1;
	private Integer y = -1;
	@Column(nullable=false) private Color color;
	
	@Override
	public String toString() {
		return String.format("[%s: %d,%d]", color, x, y);
	}
	
	@Transient
	public boolean isPass() {
		return x == null && y == null;
	}
}
