package com.topdesk.mc12.common;

import com.topdesk.mc12.rules.entities.Game;

public enum Color {
	BLACK {
		@Override
		public void addCapture(Game game, int captured) {
			game.setBlackCaptured(game.getBlackCaptured() + captured);
		}
	},
	WHITE {
		@Override
		public void addCapture(Game game, int captured) {
			game.setWhiteCaptured(game.getWhiteCaptured() + captured);
		}
	};

	public abstract void addCapture(Game game, int captured);
}
