package com.striketru.conn.base;

import java.util.Map;

import com.striketru.conn.base.model.BaseData;


public abstract class Transformer<R extends BaseData, W extends BaseData> {

	public abstract W execute(R readerData);
	
	
	protected String getStringDataFromMap(Map<String, Object> data, String key) {
		if (data != null & key != null) {
			return (data.get(key)!= null)? data.get(key).toString() : null;
		}
		return null;
	}
	
}
