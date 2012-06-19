package com.topdesk.mc12.persistence.entities;

import com.google.common.base.Function;

public interface DatabaseEntity {
	public static final Function<? extends DatabaseEntity, Long> ID_FUNCTION = new Function<DatabaseEntity, Long>() {
		@Override
		public Long apply(DatabaseEntity entity) {
			return entity.getId();
		}
	};
	
	Long getId();
}
