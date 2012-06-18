package com.topdesk.mc12.persistence.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.GameState;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class GameData implements DatabaseEntity {
	@Id @GeneratedValue private long id;
	@ManyToOne(fetch=FetchType.EAGER) private Player black;
	@ManyToOne(fetch=FetchType.EAGER) private Player white;
	@OrderBy("id") @OneToMany(fetch=FetchType.EAGER, mappedBy="game") private List<Move> moves;
	private long start;
	private BoardSize boardSize;
	private GameState state;
}
