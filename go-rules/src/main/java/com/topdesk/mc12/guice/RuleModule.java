package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.topdesk.mc12.rules.RuleEngine;
import com.topdesk.mc12.rules.GoRuleEngine;


public class RuleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RuleEngine.class).to(GoRuleEngine.class);
	}
}
