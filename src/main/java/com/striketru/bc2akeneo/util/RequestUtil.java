package com.striketru.bc2akeneo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class RequestUtil {

	private static final String[] ignoreList = {"Swatch Request Form","Google Shopping Availability","Google Shopping Condition","Google Shopping Identifer exists","Features","Google Shopping Promotion ID","Width Range","Width Unit","Height Unit","Depth Range","Depth Unit","Seath Height Unit","Weight Unit","Descriptor","Base Variant Id","Bin Picking Number","Bulk Pricing Rules","Calculated Price","Categories","Condition","Cost Price","Product Resources Shipping","Seating Width ( in.)","Seating Depth ( in.)","Seating Height ( in.)","Seating Seat Height ( in.)","Seating Arm Height ( in.)","Seating Weight (lbs.)","Seating Material","Dimensions ( in.)","Heat Output/BTUs","Any Other Relevant Info","Gift Wrapping Options List","Gift Wrapping Options Type","Height Range","Url Standard 1","Url Standard 2","Inventory Level","Inventory Tracking","Inventory Warning Level","Is Condition Shown","Is Featured","Is Free Shipping","Is Preorder Only","Layout File","Map Price","Order Quantity Maximum","Order Quantity Minimum","Preorder Message","Preorder Release Date","Primary Image File","Product Tax Code","Reviews Count","Reviews Rating Sum","Total Sold","Type","View Count","Seating Width Range","Umbrellas Width Range","Grills Width Range","Heaters Width Range","Seating Height Range","Umbrellas Height Range","Grills Height Range","Heaters Height Range","Fire Pits Height Range","Seating Depth Range","Umbrellas Depth Range","Grills Depth Range","Heaters Depth Range","Sets","Option Type","Featured Highlight","Specs Highlight","Lead-Time 2","Blacklist", "Brand","Cushions", "Height", "Width","Depth", "Use"};
	
    private static final String NULL_OR_VALUE_PATTERN = "\"%s\"";
    public static String getValue(String value){
    	if (value != null) {
    		value = String.format(NULL_OR_VALUE_PATTERN, value);
    	}
    	return value;
    }

	private static final String PIM_KET_VALUE_JSON_PATTERN = "\"%s\": \"%s\"";
    public static String createKeyValueJson(String key, String data) {
        return String.format(PIM_KET_VALUE_JSON_PATTERN, key, data);
    }
    
	private static final String PIM_KET_VALUE_LIST_JSON_PATTERN = "\"%s\": %s";
    public static String createKeyValueListJson(String key, List<String> data) {
        return String.format(PIM_KET_VALUE_LIST_JSON_PATTERN, key, data);
    }

    
	private static final String PIM_ATTR_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\":\"%s\"}]";
    public static String createAttributeJson(String key, String locale, String scope, String data) {
    	data = StringEscapeUtils.escapeHtml4(data);
        return String.format(PIM_ATTR_JSON_PATTERN, key, getValue(locale), getValue(scope), data);
    }
    
    private static final String PIM_ATTR_ARRAY_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\": %s}]";
    public static String createAttributeArrayJson(String key, String locale, String scope, ArrayList<String> data) {
    	String arrayString = StringUtils.join(data, "\", \"");
    	return String.format(PIM_ATTR_ARRAY_JSON_PATTERN, key, getValue(locale), getValue(scope), "[\""+arrayString+"\"]");
    }
    
	private static final String PIM_ATTR_UNIT_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\": {\"amount\" : \"%s\", \"unit\": \"%s\"}}]";
    public static String createAttributeUnitJson(String key, String locale, String scope, String unit, String data) {
        return String.format(PIM_ATTR_UNIT_JSON_PATTERN, key, getValue(locale), getValue(scope), data, unit);
    }

	private static final String PIM_ATTR_IMAGE_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\":\"%s\",\"_links\":{ \"download\": {\"href\":\"%s\"}}}]";
    public static String createAttributeImageJson(String key, String locale, String scope, String data, String href) {
        return String.format(PIM_ATTR_IMAGE_JSON_PATTERN, key, getValue(locale), getValue(scope), data, href);
    }

    private String createUpdateBaseProduct(Map<String, Object> data){
    	List<String> categories = (List<String>) data.get("categories");

    	String step1 = StringUtils.join(categories, "\", \"");// Join with ", "
    	String step2 = StringUtils.wrap(step1, "\"");// Wrap step1 with "
    	
    	Map<String, Object> primaryImage = (Map<String, Object>) data.get("primary_image"); 
    	System.err.println(data.get("name"));
    	StringBuilder strbuild = new StringBuilder("{");
    	
//    	strbuild.append(createKeyValueJson("product_title", data.get("name").toString())).append(",");
    	strbuild.append(createKeyValueJson("family", "Accessories_Lighting")).append(",");
    	strbuild.append("\"categories\""+":["+step2+"]").append(",");
    	strbuild.append("\"values\": {");
    	strbuild.append(createAttributeJson("brand", null, null, data.get("brand_id").toString())).append(",");
    	strbuild.append(createAttributeJson("sku_type", null, null, "B")).append(",");
//    	strbuild.append(createAttributeImageJson("image_1", null, null, primaryImage.get("image_file").toString(), primaryImage.get("url_standard").toString())).append(",");
//    	strbuild.append(createAttributeUnitJson("depth", null, null, "INCH","27.5000")).append(",");
//    	strbuild.append(createAttributeUnitJson("width", null, null, "INCH","23.5000")).append(",");
//    	strbuild.append(createAttributeUnitJson("height", null, null, "INCH","35.5000")).append(",");
    	strbuild.append(createAttributeJson("product_description", null, null, data.get("description").toString())).append(",");
    	strbuild.append(createAttributeJson("bigcommerce_id", null, null, data.get("id").toString())).append(",");
    	strbuild.append(createAttributeJson("product_title", null, null, data.get("name").toString()));
    	
    	
    	List<Object> newObj  =  (List<Object>) data.get("custom_fields");
    	if(newObj.size() > 0)
    		strbuild.append(",");
    	
    	ArrayList<String> newCategories = new ArrayList<String>();
    	for(int i=0; i<=newObj.size() - 1; i++) {
    		boolean insertComa = true;
    		Map<String, Object> customField = (Map<String, Object>) newObj.get(i);
    		System.err.println(customField.get("name").toString());
    		
    		if(Arrays.stream(ignoreList).anyMatch(customField.get("name").toString()::equals)) {
    			insertComa = false;
    		}else if(StringUtils.equals(customField.get("name").toString(), "Category")) {
    			newCategories.add(customField.get("value").toString());
    			insertComa = false;
    		}else if(StringUtils.equals(customField.get("name").toString(), "Material")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Swatch Request Form")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Price")) {
//    			strbuild.append(createAttributeJson("price_range", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Related Products")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("More Colors Icon")) {
//    			strbuild.append(createAttributeJson("more_colors_icon", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Recycle Icon")) {
//    			strbuild.append(createAttributeJson("recycle_icon", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Manufacturer Part Number")) {
    			strbuild.append(createAttributeJson("manufacturer_part_number", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Quick Ship Icon")) {
//    			strbuild.append(createAttributeJson("quick_ship_icon", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Free Shipping Icon")) {
//    			strbuild.append(createAttributeJson("free_shipping_icon", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Lead-Time")) {
    			strbuild.append(createAttributeJson("lead_time", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Lead-Time")) {
    			strbuild.append(createAttributeJson("lead_time", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Yahoo Google Shopping ID")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Yahoo Bing Shopping ID")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping MPN")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping GTIN")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping Promotion ID")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping Custom_Label_0")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping Custom_Label_1")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping shipping_label")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Yahoo Google Taxonomy")) {
    			strbuild.append(createAttributeJson("yahoo_google_taxonomy", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Google Shopping Availability")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping Condition")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Google Shopping Identifer_exists")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Trade Discount")) {
    			strbuild.append(createAttributeJson("trade_discount", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Sale Flag")) {
//    			strbuild.append(createAttributeJson("sale_flag", null, null, customField.get("value").toString()));
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Highlight 1")) {
    			strbuild.append(createAttributeJson("highlight_1", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Highlight 2")) {
    			strbuild.append(createAttributeJson("highlight_2", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Highlight 3")) {
    			strbuild.append(createAttributeJson("highlight_3", null, null, customField.get("value").toString()));
    		}else if(customField.get("name").toString().equals("Promo Text")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("PLP Promo Text")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Highlight 4")) {
    			System.err.println("****************"+i);
    		}else if(customField.get("name").toString().equals("Assembly Required")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("id")) {
    			insertComa = false;
    		}else if(customField.get("name").toString().equals("Category Excerpt")) {
    			insertComa = false;
    		}
    		else {
    			strbuild.append(createAttributeJson(customField.get("name").toString().toLowerCase(), null, null, customField.get("value").toString()));
    		}
    		if(i != newObj.size() - 1 && insertComa) {
    			strbuild.append(",");
    		}
    	}
    	StringBuilder removedComaResp = new StringBuilder();
    	if( strbuild.length() > 0 ) {
    		removedComaResp = removeComaAtEnd(strbuild.toString());
    	}
    	
    	if(newCategories.size() > 0) {
    		removedComaResp.append(",");
    		removedComaResp.append(createAttributeArrayJson("item_type", null, null, newCategories));
    	}
    	
    	removedComaResp.append("}");
    	removedComaResp.append("}");
    	System.err.println(removedComaResp);
    	return removedComaResp.toString();
    }
    
    public StringBuilder removeComaAtEnd(String strbuild) {
    	StringBuilder strBuilder = new StringBuilder(strbuild);
    	String val = strBuilder.substring(strBuilder.length() - 1);
		if(StringUtils.equals(val, ",")) {
			strBuilder.setLength(strBuilder.length() - 1);
			strBuilder = removeComaAtEnd(strBuilder.toString());
		}
		return strBuilder;
    }
    
    
	public static String request(Map<String, Object> convertedResp) {
		RequestUtil requestData = new RequestUtil();
		String baseResponse = requestData.createUpdateBaseProduct(convertedResp);
		if(StringUtils.isNotEmpty(baseResponse)) {
			requestData.createUpdateOptionProducts(convertedResp);
		}
		
		return baseResponse;
	}
	
	private String createUpdateOptionProducts(Map<String, Object> data) {
		Map<String, Object> modifiers = (Map<String, Object>) data.get("modifiers");
		Map<String, Object> optionValues = (Map<String, Object>) modifiers.get("option_values");
		Map<String, Object> value_data = (Map<String, Object>) optionValues.get("image_url");
		
		StringBuilder strbuild = new StringBuilder("{");
    	
    	strbuild.append(createKeyValueJson("display_name", modifiers.get("display_name").toString())).append(",");
    	strbuild.append(createKeyValueJson("family", "Accessories_Lighting")).append(",");
    	strbuild.append("\"values\": {");
    	strbuild.append(createAttributeJson("brand", null, null, data.get("brand_id").toString())).append(",");
    	strbuild.append(createAttributeJson("sku_type", null, null, "O")).append(",");
    	strbuild.append(createAttributeJson("product_description", null, null, data.get("description").toString())).append(",");
    	strbuild.append(createAttributeJson("label", null, null, data.get("label").toString()));
    	
    	strbuild.append("}");
    	strbuild.append("}");
		
		return strbuild.toString();
	}
}
