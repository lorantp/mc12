package com.topdesk.mc12.persistence;

import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.google.inject.Inject;
import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;

public class HibernateH2Backend implements Backend {
	private static final Class<?>[] ENTITIES = {Player.class, Move.class, Game.class, Board.class};
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
	public <T> T get(Class<T> type, long id) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		T value = (T) session.byId(type).getReference(id);
		
		if (type == Board.class) {
			Hibernate.initialize(((Board) value).getMoves());
		}
		session.getTransaction().commit();
		session.close();
		return value;
	}
	
	@Override
	public void delete(Object... entities) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : entities) {
			session.delete(object);
		}
		session.getTransaction().commit();
		session.close();
		
	}
	
	public static void main(String[] args) {
		HibernateH2Backend backend = new HibernateH2Backend("test", true, true);
		Player player = new Player(-1, "hi", "ya");
		backend.insert(player, new Move(-1, -1, -1, Color.BLACK), new Game(-1, new Board(1, BoardSize.NINETEEN.getSize(), new ArrayList<Move>()), player, player, 0, 0));
		backend.delete(backend.get(Move.class, 1));
	}
}
