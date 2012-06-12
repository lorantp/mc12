package com.topdesk.mc12.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Move {
	@Id @GeneratedValue private long id;
<<<<<<< HEAD
	@Nullable private int x = -1;
	@Nullable private int y = -1;
	@Nonnull private Color color;
=======
	@ManyToOne(optional=false) private Board board;
	private int x = -1;
	private int y = -1;
	@Column(nullable=false) private Color color;
	
	@Override
	public String toString() {
		return String.format("[%s: %d,%d]", color, x, y);
	}
>>>>>>> Fixed guice, now done (part 2)
}
