package com.topdesk.mc12.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class PersistenceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Backend.class).to(HibernateH2Backend.class).in(Scopes.SINGLETON);
	}
}
