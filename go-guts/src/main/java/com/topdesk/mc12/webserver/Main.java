package com.topdesk.mc12.webserver;

import com.google.inject.Guice;
import com.topdesk.mc12.persistence.PersistenceModule;

public class Main {
	public static void main(String[] args) throws Exception {
		Guice.createInjector(new WebModule(), new PersistenceModule());
		new WebServer().start();
	}
}
