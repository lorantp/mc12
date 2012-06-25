package com.topdesk.mc12.rest.restlets;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContext;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
@Singleton
public class LoginHelper {
	private final Provider<EntityManager> entityManager;
	private final PlayerContextMap contextMap;
	
	@Inject
	public LoginHelper(Provider<EntityManager> entityManager, PlayerContextMap contextMap) {
		this.entityManager = entityManager;
		this.contextMap = contextMap;
	}
	
	@Transactional
	public Player getOrCreate(String name) {
		String trimmedName = name.trim();
		List<Player> players = selectByName(trimmedName);
		checkState(players.size() < 2, "Player nicknames are supposed to be unique");
		
		if (players.isEmpty()) {
			Player player = Player.create(trimmedName, trimmedName + "@topdesk.com");
			entityManager.get().persist(player);
			entityManager.get().flush();
			log.info("Created new {}", player);
			return player;
		}
		
		Player player = players.get(0);
		log.info("Found existing {}", player);
		return player;
	}
	
	private List<Player> selectByName(String name) {
		EntityManager em = entityManager.get();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Player> query = builder.createQuery(Player.class);
		Root<Player> root = query.from(Player.class);
		query.select(root).where(builder.equal(builder.upper(root.get("name").as(String.class)), name.toUpperCase()));
		return em.createQuery(query).getResultList();
	}
	
	public PlayerContext login(Player player, HttpServletRequest request) {
		if (contextMap.hasContextFor(player, request)) {
			log.info("Using existing session for {}", player);
			return contextMap.getByPlayer(player, request);
		}
		log.info("Starting new session for {}", player);
		return contextMap.startNew(player, request);
	}
	
	public PlayerContext checkLogin(String id, HttpServletRequest request) {
		PlayerContext context = contextMap.getById(Integer.valueOf(id), request);
		if (context == null) {
			throw GoException.createUnauthorized("No existing context with id " + id);
		}
		return context;
	}
}
