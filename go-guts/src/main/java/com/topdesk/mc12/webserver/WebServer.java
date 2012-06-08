package com.topdesk.mc12.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class WebServer {
	private Server server;
	
	public WebServer() {
		server = new Server(80);
		WebAppContext context = new WebAppContext("../go-webserver/src/main/webapp", "/");
		server.setHandler(context);
	}
	
	public void start() throws Exception {
		server.start();
	}
}
