package com.topdesk.mc12.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.topdesk.mc12.rules.DefaultGoRuleEngine;
import com.topdesk.mc12.rules.GoRuleEngine;
import com.topdesk.mc12.rules.capturing.CaptureResolver;
import com.topdesk.mc12.rules.capturing.LiveCollectionCaptureResolver;
import com.topdesk.mc12.rules.scoring.ScoreCalculator;
import com.topdesk.mc12.rules.scoring.StoneScoreCalculator;

public class RuleModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ScoreCalculator.class).to(StoneScoreCalculator.class).in(Scopes.SINGLETON);
		bind(CaptureResolver.class).to(LiveCollectionCaptureResolver.class).in(Scopes.SINGLETON);
		bind(GoRuleEngine.class).to(DefaultGoRuleEngine.class).in(Scopes.SINGLETON);
	}
}
