package com.topdesk.mc12.common;

import javax.servlet.http.HttpServletRequest;

import com.topdesk.mc12.persistence.entities.Player;

/**
 * Stores the {@link PlayerContext} which has the information of an authorized player.
 * 
 * {@link PlayerContext}s can be retrieved from its id ({@link PlayerContext#getId()}) or via its
 * {@link Player}
 */
public interface PlayerContextMap {
	PlayerContext startNew(Player player, HttpServletRequest request);

	boolean hasContextFor(Player player, HttpServletRequest request);
	PlayerContext getByPlayer(Player player, HttpServletRequest request);
	PlayerContext getById(int contextId, HttpServletRequest request);
}
