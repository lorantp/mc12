package com.topdesk.mc12.authentication;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;

import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.persistence.entities.Player;

/**
 * Identifies a Player with a separate id.
 */
@Data(staticConstructor="create")
public class DefaultPlayerContext implements PlayerContext {
	private static AtomicInteger sessionNumber = new AtomicInteger(0);
	
	public static DefaultPlayerContext create(Player player) {
		return new DefaultPlayerContext(sessionNumber.incrementAndGet(), player);
	}
	
	private final int id;
	private final Player player;
}