package com.striketru.bc2akeneo.api;

import java.io.IOException;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

public class BcAPI {
	 public Content get() throws IOException {
	        Request request = Request.Get("https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options")
	                .addHeader("X-Auth-Client", "4ae79372faabe468ead6c41809d7448259ef33bd7e35414ee1a1589ff33719d8")
	                .addHeader("X-Auth-Token", "ng8wmxf6wr2ngw4besvytexptea2bnd")
	                .addHeader("store-hash", "r14v4z7cjw");
	        Content getResponse = request.execute().returnContent();
	        return getResponse;
	    }
}
