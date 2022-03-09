package com.striketru.bc2akeneo.transformer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.model.AttributeJson;
import com.striketru.bc2akeneo.model.ProductJson;

public class BigcommerceTransformer {
private ObjectMapper mapper = new ObjectMapper();


public void execute() throws IOException {		
	
	ObjectMapper objMapper = new ObjectMapper();
	
}

	public String transformData(List<Object> productData) throws JsonProcessingException {
	
		
		Map<String, Object> convertedProduct = new HashMap<String, Object>();
		
		
		for(Object data : productData) {
			
			Map<String, Object> convertedData = mapper.convertValue(data, Map.class);
			convertedProduct.put("identifier", convertedData.get("id"));
			convertedProduct.put("enabled", convertedData.get("is_default"));
			convertedProduct.put("family", convertedData.get("name"));
			convertedProduct.put("created", convertedData.get("date_created"));
			convertedProduct.put("updated", convertedData.get("date_modified"));
			convertedProduct.put("categories", convertedData.get("code"));
			
			Map<String, Object> values = new HashMap<String, Object>();
			Map<String, Object> value = new HashMap<String, Object>();
			values.put("name", new AttributeJson (null , convertedData.get("categories").toString(), null));
			values.put("descriptionn", new AttributeJson (null , convertedData.get("description").toString(), null));
			values.put("bc_name", new AttributeJson (null , convertedData.get("name").toString(), null));
			
			values.put("release_date", new AttributeJson (null , convertedData.get("categories").toString(), null));
			value.put("products", new ProductJson (null , convertedData.get("description").toString(), null));
			value.put("product_models", new ProductJson (null , convertedData.get("width").toString(), null));
			value.put("PACK", new ProductJson (null , convertedData.get("width").toString(), null));
			value.put("UPSELL", new ProductJson (null , convertedData.get("width").toString(), null));
			value.put("X_SELL", new ProductJson (null , convertedData.get("width").toString(), null));
			value.put("SUBSTITUTION", new ProductJson (null , convertedData.get("width").toString(), null));
			value.put("groups", new ProductJson (null , convertedData.get("categories").toString(), null));
			//values.put("description", new ProductJson (null , convertedData.get("description").toString(), null));
			//values.put("description", new AttributeJson(null , convertedData.get("description").toString(), null));
			//values.put("height", new AttributeJson(null , convertedData.get("height").toString(), null));
			//values.put("depth", new AttributeJson(null , convertedData.get("depth").toString(), null));
			value.put("groups", new ProductJson (null , convertedData.get("categories").toString(), null));
			
			
			
			convertedProduct.put("values",values);
			convertedProduct.put("associations",value);
		
			
		}
		
		return mapper.writeValueAsString(convertedProduct);		
	}

 }