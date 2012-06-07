package com.topdesk.mc12.jersey;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@XmlRootElement
public class Bean {
	private String name;
	private int id;
}
