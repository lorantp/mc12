package com.topdesk.mc12.rest.restlets;

import java.util.List;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.google.inject.servlet.RequestScoped;
import com.topdesk.mc12.common.GoException;
import com.topdesk.mc12.common.PlayerContextMap;
import com.topdesk.mc12.persistence.entities.Player;

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
	public int get(String playerName) {
		List<Player> playersFound = selectByField("name", playerName, Player.class);
		if(playersFound.size() != 1) {
			throw GoException.createBadRequest("Couldn't authenticate " + playerName);
		}
		return contextMap.startNew(playersFound.get(0)).getId();
	}

    @Transactional
	private <E> List<E> selectByField(String fieldName, String value, Class<E> entityClass) {
		EntityManager em = entityManager.get();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> unconditionalQuery = cb.createQuery(entityClass);
    	Root<E> entityStructure = unconditionalQuery.from(entityClass);
		CriteriaQuery<E> finishedQuery = unconditionalQuery.select(entityStructure).where(cb.equal(entityStructure.get(fieldName), value));
    	return em.createQuery(finishedQuery).getResultList();
	}
}
