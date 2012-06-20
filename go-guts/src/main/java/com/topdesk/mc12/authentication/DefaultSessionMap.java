package com.topdesk.mc12.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import com.topdesk.mc12.persistence.entities.Player;

public class DefaultSessionMap implements SessionMap {
	@Override
	public PlayerContext retrieveFrom(HttpServletRequest httpRequest) {
		return null;
	}

	@Override
	public PlayerContext startNew(Player player, UriInfo info) {
		PlayerContext playerContext = PlayerContext.create(player, info);
		return playerContext;
	}

	@Override
	public boolean hasFor(Player player) {
		return false;
	}

	@Override
	public PlayerContext getFor(Player player) {
		return null;
	}
}
