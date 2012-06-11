package com.topdesk.mc12.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer {
	private final Server server;
	
	public WebServer() {
		server = new Server(80);
		WebAppContext context = new WebAppContext("../go-webserver/src/main/webapp", "/");
		server.setHandler(context);
	}
	
	public void start() throws Exception {
		server.start();
	}
}
