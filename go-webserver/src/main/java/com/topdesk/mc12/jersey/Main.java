package com.topdesk.mc12.jersey;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

	public static void main(String[] args) throws Exception {
		Server server = new Server(80);
		WebAppContext context = new WebAppContext("src/main/webapp", "/");
		PackagesResourceConfig config = new PackagesResourceConfig("com.topdesk.mc12.jersey");
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		context.addServlet(new ServletHolder(new ServletContainer(config)), "/rest/*");
		context.addServlet(DefaultServlet.class, "/*");
		server.setHandler(context);
		
		server.start();
	}
}
