package com.striketru.bc2akeneo.api;

import java.io.IOException;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

public class BcAPI {
	
	public static final String authClient = "4ae79372faabe468ead6c41809d7448259ef33bd7e35414ee1a1589ff33719d8";
	public static final String authToken = "ng8wmxf6wr2ngw4besvytexptea2bnd";
	public static final String storeHash = "r14v4z7cjw";
	
	 public Content get() throws IOException {
	        Request request = Request.Get("https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options")
	                .addHeader("X-Auth-Client", authClient)
	                .addHeader("X-Auth-Token", authToken)
	                .addHeader("store-hash", storeHash);
	        Content getResponse = request.execute().returnContent();
	        return getResponse;
	    }
	 
	 public Content get(String url) throws IOException {
	        Request request = Request.Get(url)
	                .addHeader("X-Auth-Client", authClient)
	                .addHeader("X-Auth-Token", authToken)
	                .addHeader("store-hash", storeHash);
	        Content getResponse = request.execute().returnContent();
	        return getResponse;
	    }
}
