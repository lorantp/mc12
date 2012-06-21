package com.topdesk.mc12.authentication;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.Maps;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

/**
 * Simple implementation of {@link PlayerContextMap} that maintains two separate maps
 * for looking up {@link PlayerContext}s via either a {@link Player} or via its id
 */
public class DefaultPlayerContextMap implements PlayerContextMap {
	private final Map<Player, PlayerContext> playerLookup = Maps.newHashMap();
	private final Map<Integer, PlayerContext> idLookup = Maps.newHashMap();
	
	@Override
	public PlayerContext getById(int contextId) {
		return idLookup.get(contextId);
	}

	@Override
	public PlayerContext startNew(Player player) {
		PlayerContext context = DefaultPlayerContext.create(checkNotNull(player));
		playerLookup.put(player, context);
		idLookup.put(context.getId(), context);
		return context;
	}

	@Override
	public boolean hasContextFor(Player player) {
		return playerLookup.containsKey(checkNotNull(player));
	}

	@Override
	public PlayerContext getByPlayer(Player player) {
		return playerLookup.get(checkNotNull(player, "player was null, that's nothing mate."));
	}
}
