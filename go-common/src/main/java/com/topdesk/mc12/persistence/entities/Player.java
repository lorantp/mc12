package com.topdesk.mc12.persistence.entities;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public final class Player {
	@Id @GeneratedValue private long id;
	@Nonnull private String nickname;
	@Nonnull private String email;
}
