package com.striketru.bc2akeneo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.fluent.Content;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.api.BcAPI;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.bc2akeneo.common.ApplicationPropertyLoader;

@SpringBootApplication
public class Bc2akeneoApplication {
	
	public static void main(String[] args) throws Exception {
		System.err.println("Application Properties : " + System.getProperty("APP_PROP"));
		System.err.println("** Connecting to BC **");
		Content bcResponse = getBcData();
		String stringresp = bcResponse.asString();
		
		Map<String, Object> convertedResp = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			convertedResp = mapper.readValue(stringresp, Map.class);
		} catch(Exception e) {
			throw new Exception(e.getMessage());
		}
		
		System.err.println(convertedResp.get("data"));
		
		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		ProductAPI productapi = new ProductAPI(appProp.getAppProperties());
		ArrayList<Object> data =  (ArrayList<Object>) convertedResp.get("data");
		
		ArrayList<String> productResponses = new ArrayList<String>();
		ObjectMapper oMapper = new ObjectMapper();
		for (Object productData : data) {
			Map<String, Object> productDataMap = oMapper.convertValue(productData, Map.class);
			productResponses.add(productapi.createUpdateProductBySku(productDataMap, productDataMap.get("sku").toString()));
		}
		System.err.println(productResponses);
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static Content getBcData() throws IOException {
		BcAPI bcApi = new BcAPI();
		return bcApi.get();
	}
}
