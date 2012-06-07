package com.topdesk.mc12.jersey;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

	public static void main(String[] args) throws Exception {
		Server server = new Server(80);
		Context context = new Context(server, "/", Context.SESSIONS);
		context.addServlet(new ServletHolder(new ServletContainer(new PackagesResourceConfig("com.topdesk.mc12.jersey"))), "/");
		server.start();
	}
}
