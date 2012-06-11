package com.topdesk.mc12.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Game {
	@Id @GeneratedValue private long id;
	@OneToOne private Board board;
	@ManyToOne private Player black;
	@ManyToOne private Player white;
	
	/**
	 * The amount of white stones captured by the black player
	 */
	private int blackCaptured;
	
	/**
	 * The amount of black stones captured by the white player
	 */
	private int whiteCaptured;
}
