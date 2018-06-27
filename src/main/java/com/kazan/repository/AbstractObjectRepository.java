package com.kazan.repository;

import java.util.List;

public interface AbstractObjectRepository<T> {
	List<T> getAll();
	
	int deleteBySymbol(String symbol);	
	int deleteBySymbolGroup(String symbol, Integer groupId);
	int deleteBySymbolUserGroup(String symbol, Integer userId, Integer groupId);
	int deleteBySymbolUserGroupMode(String symbol, Integer userId, Integer groupId, Integer modeId);
	
	List<T> getBySymbolUserGroup(String symbol, Integer userId, Integer groupId);
	List<T> getBySymbolGroup(String symbol, Integer userId, Integer groupId);
	
	T add(T t);
	String[][] getUserIdAndUpdateTime(String symbol, Integer groupId);
}
