package com.topdesk.mc12.webserver;

import lombok.SneakyThrows;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServer {
	static {
		System.setProperty("GoPersistence", "com.topdesk.mc12.go.test");
	}
	
	private final Server server;
	
	public WebServer() {
		server = new Server(80);
		WebAppContext context = new WebAppContext("src/main/webapp", "/");
		server.setHandler(context);
	}
	
	@SneakyThrows(Exception.class)
	public void start() {
		server.start();
	}
	
	@SneakyThrows(Exception.class)
	public void stop() {
		server.stop();
	}
	
	public static void main(String[] args) {
		new WebServer().start();
	}
}
