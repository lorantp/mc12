package com.topdesk.mc12.common;

import com.topdesk.mc12.persistence.entities.Player;

/**
 * Stores the {@link PlayerContext} which has the information of an authorized player.
 * 
 * {@link PlayerContext}s can be retrieved from its id ({@link PlayerContext#getId()}) or via its
 * {@link Player}
 */
public interface PlayerContextMap {
	PlayerContext startNew(Player player);

	boolean hasContextFor(Player player);
	PlayerContext getByPlayer(Player player);
	PlayerContext getById(int contextId);
}
