package com.striketru.conn.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.striketru.conn.base.model.BaseData;


public abstract class Transformer<R extends BaseData, W extends BaseData> {

	public abstract W execute(R readerData);
	private static int newId = 1000000;
	private Map<String, Set<String>> multiSelectData;
	
	protected static String getNextId() {
		newId++;
		return String.valueOf(newId);
	}
	
	protected void addMultiSelectData(String key, String Value) {
		if (multiSelectData == null) {
			multiSelectData = new HashMap<>();
		}
		if (Value != null) { 
			if (multiSelectData.get(key) != null) {
				multiSelectData.get(key).add(Value);
			} else {
				multiSelectData.put(key, new HashSet<>());
				multiSelectData.get(key).add(Value);
			}
		}
	}
	protected Map<String, Set<String>> getMultiSelectData(){
		return multiSelectData;
	}
	
	protected String getStringDataFromMap(Map<String, Object> data, String key) {
		if (data != null & key != null) {
			return (data.get(key)!= null)? data.get(key).toString() : null;
		}
		return null;
	}
	
}
