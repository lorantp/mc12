package com.topdesk.mc12.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("bye")
public class Goodbye {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String bye() {
		return "<html><body><form action=\"bye\" method=\"post\"><input type=\"date\" name=\"mc12\"><input type=\"submit\" name=\"mysubmit\" value=\"Click!\" /></form></body></html>";
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String postIt(@FormParam("mc12") String param) {
		return "<html><body>received "+param+"</body></html>";
	}
}
