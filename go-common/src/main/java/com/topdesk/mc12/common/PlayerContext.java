package com.topdesk.mc12.common;

import com.topdesk.mc12.persistence.entities.Player;

/**
 * TODO Is this entire PlayerContext shell required at all?
 */
public interface PlayerContext {
	Player getPlayer();
	int hashCode();
}
