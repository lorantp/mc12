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
	@Nullable private int x = -1;
	@Nullable private int y = -1;
	@Nonnull private Color color;
}
