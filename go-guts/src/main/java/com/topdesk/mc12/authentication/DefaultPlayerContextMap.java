package com.topdesk.mc12.authentication;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Maps;
import com.topdesk.mc12.persistence.entities.Player;

public class DefaultPlayerContextMap implements PlayerContextMap {
	private Map<Player, PlayerContext> playerLookup = Maps.newHashMap();
	private Map<Integer, PlayerContext> idLookup = Maps.newHashMap();
	
	@Override
	public PlayerContext retrieveFrom(HttpServletRequest httpRequest) {
		Object contextIdAttribute = httpRequest.getAttribute("contextid");
		if (contextIdAttribute == null) {
			throw new IllegalArgumentException("Request contained no sessionId");
		}
		Integer contextId = Integer.valueOf((String) contextIdAttribute);
		return idLookup.get(contextId);
	}

	@Override
	public PlayerContext startNew(Player player, UriInfo info) {
		PlayerContext context = PlayerContext.create(checkNotNull(player), info);
		playerLookup.put(player, context);
		idLookup.put(context.getId(), context);
		return context;
	}

	@Override
	public boolean hasContextFor(Player player) {
		return playerLookup.containsKey(checkNotNull(player));
	}

	@Override
	public PlayerContext getContextFor(Player player) {
		return playerLookup.get(checkNotNull(player, "player was null, that's nothing mate."));
	}
}
