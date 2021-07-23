package com.striketru.pim.util;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;

public interface PIMRequestUtil {

	static Gson gson = new Gson();
	
    static final String NULL_OR_VALUE_PATTERN = "\"%s\"";
    public default String getValue(String value){
    	if (value != null) {
    		value = String.format(NULL_OR_VALUE_PATTERN, StringEscapeUtils.escapeJson(value));
    	}
    	return value;
    }

	static final String PIM_KET_VALUE_JSON_PATTERN = "\"%s\": %s";
    public default String createKeyValueJson(String key, String data) {
   		return String.format(PIM_KET_VALUE_JSON_PATTERN, key, getValue(data));
    }
    
	static final String PIM_KET_VALUE_LIST_JSON_PATTERN = "\"%s\": %s";
    public default String createKeyValueListJson(String key, List<String> data) {
        return String.format(PIM_KET_VALUE_LIST_JSON_PATTERN, key, gson.toJson(data));
    }

    
	static final String PIM_ATTR_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\":%s}]";
    public default String createAttributeJson(String key, String locale, String scope, String data) {
  		return String.format(PIM_ATTR_JSON_PATTERN, key, getValue(locale), getValue(scope), getValue(data) );
    }
    public default String createAttributeJson(String key, String locale, String scope, String data, boolean isDataInQuotes) {
    	if (!isDataInQuotes) { 
    		return String.format(PIM_ATTR_JSON_PATTERN, key, getValue(locale), getValue(scope), data );
    	} else {
    		return String.format(PIM_ATTR_JSON_PATTERN, key, getValue(locale), getValue(scope), getValue(data) );
    	}
    }
    
    static final String PIM_ATTR_ARRAY_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\": %s}]";
    public default String createAttributeArrayJson(String key, String locale, String scope, List<String> data) {
    	return String.format(PIM_ATTR_ARRAY_JSON_PATTERN, key, getValue(locale), getValue(scope), gson.toJson(data));
    }
    
	static final String PIM_ATTR_UNIT_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\": {\"amount\" : %s, \"unit\": \"%s\"}}]";
    public default String createAttributeUnitJson(String key, String locale, String scope, String unit, String data) {
        return String.format(PIM_ATTR_UNIT_JSON_PATTERN, key, getValue(locale), getValue(scope), getValue(data), unit);
    }

	static final String PIM_ATTR_IMAGE_JSON_PATTERN = "\"%s\":[{\"scope\":%s, \"locale\":%s, \"data\":\"%s\",\"_links\":{ \"download\": {\"href\":\"%s\"}}}]";
    public default String createAttributeImageJson(String key, String locale, String scope, String href, String data) {
        return String.format(PIM_ATTR_IMAGE_JSON_PATTERN, key, getValue(locale), getValue(scope), data, href);
    }
    
	static final String PIM_MEDIA_PRODUCT_JSON_PATTERN = "{\"identifier\":\"%s\", \"attribute\":\"%s\", \"locale\":%s,\"scope\":%s}";
    public default String createMediaProductJson(String identifier, String attribute,  String locale, String scope) {
        return String.format(PIM_MEDIA_PRODUCT_JSON_PATTERN, identifier, attribute, getValue(locale), getValue(scope));
    }

	static final String PIM_MEDIA_PRODUCT_MODEL_JSON_PATTERN = "{\"code\":\"%s\", \"attribute\":\"%s\", \"locale\":%s,\"scope\":%s}";
    public default String createMediaProductModelJson(String code, String attribute,  String locale, String scope) {
        return String.format(PIM_MEDIA_PRODUCT_MODEL_JSON_PATTERN, code, attribute, getValue(locale), getValue(scope));
    }
    

    
}
