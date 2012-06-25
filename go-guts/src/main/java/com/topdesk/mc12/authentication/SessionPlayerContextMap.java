package com.topdesk.mc12.authentication;

import static com.google.common.base.Preconditions.*;

import java.util.Collections;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@Singleton
public class SessionPlayerContextMap implements PlayerContextMap {
	@Override
	public PlayerContext getById(int contextId, HttpServletRequest request) {
		return (PlayerContext) request.getSession().getAttribute(Integer.toString(contextId));
	}

	@Override
	public PlayerContext startNew(Player player, HttpServletRequest request) {
		PlayerContext context = DefaultPlayerContext.create(checkNotNull(player));
		request.getSession().setAttribute(Integer.toString(context.hashCode()), context);
		return context;
	}

	@Override
	public boolean hasContextFor(Player player, HttpServletRequest request) {
		return findPlayerContext(checkNotNull(player), request) != null;
	}

	@Override
	public PlayerContext getByPlayer(Player player, HttpServletRequest request) {
		return findPlayerContext(player, request);
	}
	
	private PlayerContext findPlayerContext(Player player, HttpServletRequest request) {
		for (String id : Collections.list(request.getSession().getAttributeNames())) {
			Object attribute = request.getSession().getAttribute(id);
			if (attribute instanceof PlayerContext && ((PlayerContext) attribute).getPlayer().equals(player)) {
				return (PlayerContext) attribute;
			}
		}
		return null;
	}
}
