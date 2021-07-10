package com.striketru.bc2akeneo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.api.BcAPI;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.bc2akeneo.common.ApplicationPropertyLoader;
import com.striketru.bc2akeneo.util.RequestUtil;

//@SpringBootApplication
public class Bc2akeneoApplication {

	RequestUtil akeneoUtil = new RequestUtil();
	ObjectMapper mapper = new ObjectMapper();
	
	public void execute(){
		
		Map<String, Object> bcResp = null;
		System.out.println("Application Properties : " + System.getProperty("APP_PROP"));
		System.out.println("** Connecting to BC **");
			

		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		ProductAPI productapi = new ProductAPI(appProp.getAppProperties());
		
		
		try {
			
//			List<Object> data =  getBcData();
			List<Object> data =  getBcData("9147");

			List<String> productResponses = new ArrayList<>();
			ObjectMapper oMapper = new ObjectMapper();
			for (Object productData : data) {
				Map<String, Object> productDataMap = oMapper.convertValue(productData, Map.class);
				String baseProductRequest = akeneoUtil.createUpdateBaseProduct(productDataMap);
				System.out.println("Request : " + baseProductRequest);
				productapi.upsertProductBySku(productDataMap.get("sku").toString(), baseProductRequest);
				String optionProductRequest = akeneoUtil.createUpdateOptionProducts(productDataMap.get("sku").toString(), productDataMap);
				if (StringUtils.isNoneEmpty(optionProductRequest)){
					productapi.upsertMutipleProducts(optionProductRequest);
				}
			}
			System.out.println(productResponses);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public List<Object> getBcData() throws IOException {
		BcAPI bcApi = new BcAPI();
		String url = "https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products/9147?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options";
		Content bcResponse = bcApi.get(url);
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		System.out.println(bcResp.get("data"));
		return  (List<Object>) bcResp.get("data");
	}
	

	public List<Object> getBcData(String id) throws IOException {
		BcAPI bcApi = new BcAPI();
		String url = "https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products/"+ id + "?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options";
		Content bcResponse = bcApi.get(url);
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		System.out.println(bcResp.get("data"));
		List<Object> listObj = new ArrayList<>();
		listObj.add(bcResp.get("data"));
		return listObj;
	}
	
	
	//	public String createrequest(Map<String, Object> convertedResp) {
//		updateProductBySku
//		if(StringUtils.isNotEmpty(baseResponse)) {
//			createUpdateOptionProducts(convertedResp);
//		}
//		
//		return baseResponse;
//	}

	public static void main(String[] args) throws Exception {
		new Bc2akeneoApplication().execute();
	}

}
