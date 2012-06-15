package com.topdesk.mc12.persistence;

import java.util.List;

import com.topdesk.mc12.persistence.entities.DatabaseEntity;

public interface Backend {
	void insert(Object... entities);
	void update(Object... entities);
	void delete(Object... entities);
	void delete(Iterable<? extends Object> entities);
	<T extends DatabaseEntity> T get(Class<T> type, long id);
	<T extends DatabaseEntity> List<T> get(Class<T> type, Iterable<Long> ids);
}
