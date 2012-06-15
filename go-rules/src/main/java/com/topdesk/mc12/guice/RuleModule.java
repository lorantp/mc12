package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.topdesk.mc12.rules.DefaultGoRuleEngine;


public class RuleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DefaultGoRuleEngine.class).to(DefaultGoRuleEngine.class);
	}
}
