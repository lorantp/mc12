package com.topdesk.mc12.persistence.entities;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public final class Player implements DatabaseEntity {
	public static Player create(String nickname, String email) {
		Player player = new Player();
		player.setNickname(nickname);
		player.setEmail(email);
		return player;
	}
	
	@Id @GeneratedValue private Long id;
	@Nonnull private String nickname;
	@Nonnull private String email;
}
