package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.topdesk.mc12.rules.DefaultGoRuleEngine;
import com.topdesk.mc12.rules.GoRuleEngine;

public class RuleModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(GoRuleEngine.class).to(DefaultGoRuleEngine.class);
	}
}
