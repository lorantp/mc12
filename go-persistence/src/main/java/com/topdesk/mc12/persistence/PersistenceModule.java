package com.topdesk.mc12.persistence;

import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule{
	@Override
	protected void configure() {
		bind(Backend.class).to(H2Backend.class);
	}
}
