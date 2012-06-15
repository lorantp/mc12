package com.topdesk.mc12.persistence;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.inject.Inject;
import com.topdesk.mc12.persistence.entities.DatabaseEntity;
import com.topdesk.mc12.persistence.entities.GameData;
import com.topdesk.mc12.persistence.entities.Move;
import com.topdesk.mc12.persistence.entities.Player;

public class HibernateH2Backend implements Backend {
	private static final Class<?>[] ENTITIES = {Player.class, Move.class, GameData.class};
	private final SessionFactory factory;
	
	@Inject
	public HibernateH2Backend() {
		this("test", true, true);
	}
	
	public HibernateH2Backend(String dbName, boolean inMemory, boolean debug) {
		Configuration configuration = new Configuration().setProperty("hibernate.hbm2ddl.auto", "update");
		
		for (Class<?> entity : ENTITIES) {
			configuration.addAnnotatedClass(entity);
		}
		
		this.factory = configuration
				.buildSessionFactory(new ServiceRegistryBuilder()
						.applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
						.applySetting("hibernate.connection.driver_class", "org.h2.Driver")
						.applySetting("hibernate.connection.url", "jdbc:h2:" + (inMemory ? "mem:" : "") + dbName + ";DB_CLOSE_DELAY=-1;MVCC=TRUE")
						.applySetting("hibernate.show_sql", debug)
						.buildServiceRegistry());
	}
	
	@Override
	@Transactional
	public void update(Object... entities) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : entities) {
			session.update(object);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public void insert(Object... entities) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : entities) {
			session.save(object);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public <T extends DatabaseEntity> T get(Class<T> type, long id) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		T value = (T) session.byId(type).getReference(id);
		session.getTransaction().commit();
		session.close();
		return value;
	}
	
	@Override
	public <T extends DatabaseEntity> List<T> get(Class<T> type, Iterable<Long> ids) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		Builder<T> builder = ImmutableList.builder();
		for (long id : ids) {
			@SuppressWarnings("unchecked")
			T object = (T) session.byId(type).getReference(id);
			builder.add(object);
		}
		
		session.getTransaction().commit();
		session.close();
		return builder.build();
	}
	
	@Override
	public void delete(Object... entities) {
		delete(Arrays.asList(entities));
	}
	
	@Override
	public void delete(Iterable<? extends Object> entities) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : entities) {
			session.delete(object);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public static void main(String[] args) {
		new HibernateH2Backend();
	}
}
