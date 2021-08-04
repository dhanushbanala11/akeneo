package com.striketru.bc2akeneo.reader;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.conn.base.model.BaseData;

public class ReaderData extends BaseData {
	ObjectMapper objMapper = new ObjectMapper();
	private Map<String, Object> product;
	
	@SuppressWarnings("unchecked")
	public ReaderData(Object obj) {
		this.product = objMapper.convertValue(obj, Map.class);
		this.setSku(product.get("sku").toString());
		this.setBcId(product.get("id").toString());
	}

	public Map<String, Object> getProduct() {
		return product;
	}

	public void setProduct(Map<String, Object> product) {
		this.product = product;
	}
	
}
