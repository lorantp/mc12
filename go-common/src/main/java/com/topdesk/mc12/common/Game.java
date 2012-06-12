package com.topdesk.mc12.common;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Game {
	@Id @GeneratedValue private long id;
	@OneToOne(fetch=FetchType.EAGER) private Board board;
	@ManyToOne(fetch=FetchType.EAGER) private Player black;
	@ManyToOne(fetch=FetchType.EAGER) private Player white;
	private long start;
	
	/**
	 * The amount of white stones captured by the black player
	 */
	private int blackCaptured;
	
	/**
	 * The amount of black stones captured by the white player
	 */
	private int whiteCaptured;
	
	@Transient
	public DateTime getStartTime() {
		return new DateTime(start);
	}
	
	public void setStartTime(DateTime startTime) {
		setStart(startTime.getMillis());
	}
}
