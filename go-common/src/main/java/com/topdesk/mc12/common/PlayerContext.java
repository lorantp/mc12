package com.topdesk.mc12.common;

import com.topdesk.mc12.persistence.entities.Player;

public interface PlayerContext {
	Player getPlayer();
	int getId();
}
