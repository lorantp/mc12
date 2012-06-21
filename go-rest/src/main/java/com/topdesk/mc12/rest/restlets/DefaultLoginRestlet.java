package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

@Slf4j
@RequestScoped
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
	public int get(String playerName) {
		List<Player> playersFound = selectByField("name", playerName, Player.class);
		
		if (playersFound.isEmpty()) {
			Player player = Player.create(playerName, playerName + "@topdesk.com");
			entityManager.get().persist(player);
			log.info("Created new player and logged in for {}", player);
			return contextMap.startNew(player).getId();
		}
		
		Player player = playersFound.get(0);
		if (contextMap.hasContextFor(player)) {
			log.info("Using existing login for {}", player);
			return contextMap.getByPlayer(player).getId();
		}
		log.info("Logged in for {}", player);
		return contextMap.startNew(player).getId();
	}
	
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entity) {
		EntityManager em = entityManager.get();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(entity);
		Root<E> root = query.from(entity);
		query.select(root).where(builder.equal(root.get(fieldName), value));
		return em.createQuery(query).getResultList();
	}
}
