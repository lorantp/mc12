package com.topdesk.mc12.jersey;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Bean {
	private String name;
	private int id;
	private Map<String, Object> map;
}
