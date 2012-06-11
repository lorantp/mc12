package com.topdesk.mc12.common;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Board {
	@Id @GeneratedValue private long id;
	@ManyToOne private Game game;
	@OneToMany private List<Move> moves;
}
