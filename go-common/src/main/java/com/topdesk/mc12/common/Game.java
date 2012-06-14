package com.topdesk.mc12.common;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Game {
	@Id @GeneratedValue private long id;
	@ManyToOne(fetch=FetchType.EAGER) private Player black;
	@ManyToOne(fetch=FetchType.EAGER) private Player white;
	@OneToMany(fetch=FetchType.EAGER, mappedBy="game") private List<Move> moves;
	private long start;
	@JsonIgnore private BoardSize boardSize;
	
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
	
	public int getSize() {
		return boardSize.getSize();
	}
}
