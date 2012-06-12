package com.topdesk.mc12.rest;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import com.sun.jersey.api.core.DefaultResourceConfig;

@Singleton
@ApplicationPath("/rest")
public class RestInterfaceConfig extends DefaultResourceConfig {

}
