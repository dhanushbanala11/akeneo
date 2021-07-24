package com.striketru.bc2akeneo.reader;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.constants.Constants;
import com.striketru.bc2akeneo.writer.WriterData;
import com.striketru.conn.base.model.BaseData;

public class ReaderData extends BaseData {
	ObjectMapper objMapper = new ObjectMapper();
	private Map<String, Object> product;
	
	@SuppressWarnings("unchecked")
	public ReaderData(Object obj) {
		this.product = objMapper.convertValue(obj, Map.class);
		this.setSku(product.get("sku").toString());
	}

	public Map<String, Object> getProduct() {
		return product;
	}

	public void setProduct(Map<String, Object> product) {
		this.product = product;
	}
	
}
