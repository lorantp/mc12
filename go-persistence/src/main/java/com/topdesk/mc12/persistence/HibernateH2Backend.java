package com.topdesk.mc12.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.topdesk.mc12.common.Board;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Color;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Move;
import com.topdesk.mc12.common.Player;

public class HibernateH2Backend implements Backend {
	private final SessionFactory factory;
	
	public HibernateH2Backend(String dbName, boolean inMemory, boolean debug, Class<?>... entities) {
		String url = String.format("jdbc:h2:%s%s;DB_CLOSE_DELAY=-1;MVCC=TRUE", inMemory ? "mem:" : "", dbName);
		
		Configuration configuration = new Configuration().setProperty(
				"hibernate.hbm2ddl.auto", "update");
		for (Class<?> entity : entities) {
			configuration.addAnnotatedClass(entity);
		}
		this.factory = configuration
				.buildSessionFactory(new ServiceRegistryBuilder()
						.applySetting("hibernate.dialect",
								"org.hibernate.dialect.H2Dialect")
						.applySetting("hibernate.connection.driver_class",
								"org.h2.Driver")
						.applySetting("hibernate.connection.url", url)
						.applySetting("hibernate.show_sql", debug)
						.buildServiceRegistry());
	}
	
	@Override
	public void update(Object... objects) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : objects) {
			session.update(object);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	@Override
	public void insert(Object... objects) {
		Session session = factory.openSession();
		session.beginTransaction();
		for (Object object : objects) {
			session.save(object);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public static void main(String[] args) {
		HibernateH2Backend backend = new HibernateH2Backend("test", false, true, Player.class, Move.class, Game.class, Board.class);
		Player player = new Player(0, "hi", "ya");
		backend.insert(player, new Move(-1, -1, -1, Color.BLACK), new Game(-1, BoardSize.NINETEEN, player, player, 0, 0));
	}
}
