package com.striketru.bc2akeneo.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BigCommAPI {

    private static final Logger LOGGER = LogManager.getLogger(BigCommAPI.class);

    private static final String PRODUCT_URL= "https://api.bigcommerce.com/stores/%s/v3/catalog/products/%s?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options";
	private String authClient;
	private String authToken;
	private String storeHash;
	private String url;
	private String pagedUrl;
	private ObjectMapper mapper = new ObjectMapper();
	
	public BigCommAPI(String authClient, String authToken, String storeHash, String url) {
		this.authClient = authClient;
		this.authToken = authToken;
		this.storeHash = storeHash;
		this.url = url;
		this.pagedUrl = url+"&page=%d&limit=10";
	}

	public Content get(String url) throws IOException {
		Request request = Request.Get(url)
				.addHeader("X-Auth-Client", getAuthClient())
				.addHeader("X-Auth-Token", getAuthToken())
				.addHeader("store-hash", getStoreHash());
		Content getResponse = request.execute().returnContent();
		return getResponse;
	}
	
	@SuppressWarnings("unchecked")
	public int getDataPageCount() throws IOException {
		String pages = "";
		Content bcResponse = get(url);
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		if (bcResp.get("meta") != null) {
			Map<String, Object> meta = (Map<String, Object>)bcResp.get("meta");
			Map<String, Object> pagination = (Map<String, Object>)meta.get("pagination");
			pages = pagination.get("total_pages").toString();
			LOGGER.info("Total Pages: " + pages);
		}
		return  StringUtils.isNotEmpty(pages) ? Integer.valueOf(pages) : 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getDataByPage(int pageNum) throws IOException {
		Content bcResponse = get(String.format(pagedUrl, pageNum));
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		LOGGER.debug(bcResp.get("data"));
		return  (List<Object>) bcResp.get("data");
	}

	/*
	 * Temp method
	 */
	public List<Object> getData(String... ids ) throws IOException {
		List<Object> listObj = new ArrayList<>();
		for (String id : ids) {
			Content bcResponse = get(String.format(PRODUCT_URL, getStoreHash(), id));
			LOGGER.debug(bcResponse);

			Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
			LOGGER.debug(bcResp.get("data"));
			listObj.add(bcResp.get("data"));
		}
		return listObj;
	}
	
	public String getAuthClient() {
		return authClient;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getStoreHash() {
		return storeHash;
	}

	public String getUrl() {
		return url;
	}

	

}
