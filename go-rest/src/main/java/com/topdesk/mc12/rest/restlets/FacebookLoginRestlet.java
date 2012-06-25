package com.topdesk.mc12.rest.restlets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("login/facebook")
public interface FacebookLoginRestlet {
	@GET
	Response login(@QueryParam("code") String code);
}
