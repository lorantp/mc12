package com.topdesk.mc12.authentication;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.SecurityContext;

import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.persistence.entities.Player;

@RequestScoped
public class PlayerProvider implements Provider<Player> {
	private final SecurityContext sc;
	
	@Inject
	public PlayerProvider(SecurityContext sc) {
		this.sc = sc;
	}
	
	@Override
	public Player get() {
		return (Player) sc.getUserPrincipal();
	}
}
