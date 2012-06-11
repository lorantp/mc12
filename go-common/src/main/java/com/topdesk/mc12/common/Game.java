package com.topdesk.mc12.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Game {
	@Id @GeneratedValue private long id;
	private BoardSize size;
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
