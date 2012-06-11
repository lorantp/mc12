package com.topdesk.mc12.persistence;

public interface Backend {
	void insert(Object... entities);
	void update(Object... entities);
	void delete(Object... entities);
	<T> T get(Class<T> type, long id);
}
