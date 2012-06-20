package com.topdesk.mc12.persistence.entities;

import java.security.Principal;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@Entity
public final class Player implements DatabaseEntity, Principal {
	public static Player create(String nickname, String email) {
		Player player = new Player();
		player.setNickname(nickname);
		player.setEmail(email);
		return player;
	}
	
	@Id @GeneratedValue private Long id;
	@Column(nullable=false, unique=true) private String nickname;
	@Nonnull private String email;
	@Override
	public String getName() {
		return nickname;
	}
}
