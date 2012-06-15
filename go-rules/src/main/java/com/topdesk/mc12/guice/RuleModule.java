package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.topdesk.mc12.rules.CaptureResolver;
import com.topdesk.mc12.rules.DefaultGoRuleEngine;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.LiveCollectionCaptureResolver;

public class RuleModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(CaptureResolver.class).to(LiveCollectionCaptureResolver.class);
		bind(GoRuleEngine.class).to(DefaultGoRuleEngine.class);
	}
}
