package com.topdesk.mc12.authentication;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.persistence.entities.Player;

/**
 * SecurityContext used to perform authorization checks.
 */
public @Data @RequiredArgsConstructor(access=AccessLevel.PRIVATE) class DefaulPlayerContext implements PlayerContext {
	private static AtomicInteger sessionNumber = new AtomicInteger(0);
	
	public static DefaulPlayerContext create(Player player) {
		return new DefaulPlayerContext(sessionNumber.incrementAndGet(), player);
	}
	
	private final int id;
    private final Player player;
}