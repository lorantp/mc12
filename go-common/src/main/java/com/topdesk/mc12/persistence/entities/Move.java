package com.topdesk.mc12.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonIgnore;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(exclude="game")
@Entity
public final class Move implements DatabaseEntity {
	@Id @GeneratedValue private long id;
	@JsonIgnore @Getter(AccessLevel.PRIVATE) @ManyToOne(optional=false) private GameData game;
	private Integer x;
	private Integer y;
	@JsonIgnore @ManyToOne(optional=false, fetch=FetchType.EAGER) private Player player;
	
	@Override
	public String toString() {
		if (isPass()) {
		}
		return String.format("[%s: %d,%d]", player.getNickname(), x, y);
	}
	
	@Transient
	public boolean isPass() {
		return x == null && y == null;
	}
	
	@Transient
	public Color getColor() {
		return getGame().getBlack().getId() == getPlayer().getId() ? Color.BLACK : Color.WHITE;
	}
}
