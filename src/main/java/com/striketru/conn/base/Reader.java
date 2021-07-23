package com.striketru.conn.base;

import com.striketru.conn.base.model.BaseData;

public abstract class Reader {

	BaseData readerData;
	Transformer transformer;
	Writer writer;
	
	public abstract void execute();
	
}
