package com.topdesk.mc12.rest.restlets;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.persistence.entities.Player;
import com.topdesk.mc12.rest.entities.ContextData;

public class DefaultContextLoginRestlet implements ContextLoginRestlet {
	private final LoginHelper loginHelper;
	
	@Inject
	public DefaultContextLoginRestlet(LoginHelper loginHelper) {
		this.loginHelper = loginHelper;
	}
	
	@Override
	@Transactional
	public ContextData get(HttpServletRequest request, String playerName) {
		Player player = loginHelper.getOrCreate(playerName);
		PlayerContext context = loginHelper.login(player, request);
		return createData(context);
	}
	
	@Override
	public ContextData checkId(HttpServletRequest request, String id) {
		PlayerContext context = loginHelper.checkLogin(id, request);
		return createData(context);
	}
	
	private ContextData createData(PlayerContext context) {
		return new ContextData(context.hashCode(), context.getPlayer().getId());
	}
}
