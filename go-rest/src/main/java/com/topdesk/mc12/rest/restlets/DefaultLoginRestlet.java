package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
public class DefaultLoginRestlet implements LoginRestlet {
	private final Provider<EntityManager> entityManager;
	private final PlayerContextMap contextMap;
	
	@Inject
	public DefaultLoginRestlet(Provider<EntityManager> entityManager, PlayerContextMap contextMap) {
		this.entityManager = entityManager;
		this.contextMap = contextMap;
	}
	
	@Override
	@Transactional
	public int get(HttpServletRequest request, String playerName) {
		List<Player> players = selectByField("name", playerName, Player.class);
		
		if (players.isEmpty()) {
			Player player = Player.create(playerName, playerName + "@topdesk.com");
			entityManager.get().persist(player);
			log.info("Created new player and logged in for {}", player);
			return contextMap.startNew(player, request).hashCode();
		}
		
		Player player = players.get(0);
		if (contextMap.hasContextFor(player, request)) {
			log.info("Using existing login for {}", player);
			return contextMap.getByPlayer(player, request).hashCode();
		}
		log.info("Logged in for {}", player);
		return contextMap.startNew(player, request).hashCode();
	}
	
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entity) {
		EntityManager em = entityManager.get();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(entity);
		Root<E> root = query.from(entity);
		query.select(root).where(builder.equal(builder.upper(root.get(fieldName).as(String.class)), value.toUpperCase()));
		return em.createQuery(query).getResultList();
	}
}
