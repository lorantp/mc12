package com.topdesk.mc12.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/postjson")
public class JsonPostAjax {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String bye() {
		return "<html>" +
				"<head> <script src=\"/js/libs/jquery-1.7.1.js\"> </script></head> " +
				"<body onload=\"$.ajax({type:'post', url: 'receivejson', dataType: 'json', data: {'id': 123, 'nickname':'Bernd'}, success: function() {alert('TEST')}});\"> post done </body>" +
				"</html>";
	}
}
