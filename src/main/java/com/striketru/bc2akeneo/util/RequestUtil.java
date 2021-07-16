package com.striketru.bc2akeneo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.striketru.bc2akeneo.constants.BuilderConstants;
import com.striketru.bc2akeneo.model.WriteResult;

public class RequestUtil {
	public static Gson gson = new Gson();
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
        return String.format(PIM_KET_VALUE_LIST_JSON_PATTERN, key, gson.toJson(data));
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

	private static final String PIM_ATTR_IMAGE_JSON_PATTERN = "\"%s\":[{\"scope\":%s, \"locale\":%s, \"data\":\"%s\",\"_links\":{ \"download\": {\"href\":\"%s\"}}}]";
    public static String createAttributeImageJson(String key, String locale, String scope, String data, String href) {
        return String.format(PIM_ATTR_IMAGE_JSON_PATTERN, key, getValue(locale), getValue(scope), data, href);
    }
    
	private static final String PIM_MEDIA_PRODUCT_JSON_PATTERN = "{\"identifier\":\"%s\", \"attribute\":\"%s\", \"locale\":%s,\"scope\":%s}";
    public static String createMediaProductJson(String identifier, String attribute,  String locale, String scope) {
        return String.format(PIM_MEDIA_PRODUCT_JSON_PATTERN, identifier, attribute, getValue(locale), getValue(scope));
    }

	private static final String PIM_MEDIA_PRODUCT_MODEL_JSON_PATTERN = "{\"code\":\"%s\", \"attribute\":\"%s\", \"locale\":%s,\"scope\":%s}";
    public static String createMediaProductModelJson(String code, String attribute,  String locale, String scope) {
        return String.format(PIM_MEDIA_PRODUCT_MODEL_JSON_PATTERN, code, attribute, getValue(locale), getValue(scope));
    }
    

    public String createUpdateBaseProduct(Map<String, Object> data, boolean isOptionProductsExists){
    	List<String> categories = (List<String>) data.get("categories");

    	String step1 = StringUtils.join(categories, "\", \"");// Join with ", "
    	String step2 = StringUtils.wrap(step1, "\"");// Wrap step1 with "
    	
    	Map<String, Object> primaryImage = (Map<String, Object>) data.get("primary_image"); 
    	System.out.println(data.get("name"));
    	StringBuilder strbuild = new StringBuilder("{");
//    	strbuild.append(createKeyValueJson("product_title", data.get("name").toString())).append(",");
    	
    	strbuild.append(createKeyValueJson("family", "Accessories_Lighting"));
//    	strbuild.append("\"categories\""+":["+step2+"]").append(",");
    	strbuild.append(",").append("\"values\": {");
    	strbuild.append(createAttributeJson("brand", null, null, data.get("brand_id").toString()));
    	strbuild.append(",").append(createAttributeJson("sku_type", null, null, "B"));
//    	strbuild.append(",").append(createAttributeImageJson("image_1", null, null, primaryImage.get("image_file").toString(), primaryImage.get("url_standard").toString())).append(",");
//    	strbuild.append(",").append(createAttributeUnitJson("depth", null, null, "INCH","27.5000")).append(",");
//    	strbuild.append(",").append(createAttributeUnitJson("width", null, null, "INCH","23.5000")).append(",");
//    	strbuild.append(",").append(createAttributeUnitJson("height", null, null, "INCH","35.5000")).append(",");
    	strbuild.append(",").append(createAttributeJson("product_description", null, null, data.get("description").toString()));
    	strbuild.append(",").append(createAttributeJson("bigcommerce_id", null, null, data.get("id").toString()));
    	strbuild.append(",").append(createAttributeJson("product_title", null, null, data.get("name").toString()));
    	
    	
    	List<Object> newObj  =  (List<Object>) data.get("custom_fields");
    	if(newObj!=null && newObj.size() > 0)
    		strbuild.append(",");
    	
//    	ArrayList<String> newCategories = new ArrayList<String>();
//    	for(int i=0; i<=newObj.size() - 1; i++) {
//    		boolean insertComa = true;
//    		Map<String, Object> customField = (Map<String, Object>) newObj.get(i);
//    		System.out.println(customField.get("name").toString());
//    		
//    		if(Arrays.stream(ignoreList).anyMatch(customField.get("name").toString()::equals)) {
//    			insertComa = false;
//    		}else if(StringUtils.equals(customField.get("name").toString(), "Category")) {
//    			newCategories.add(customField.get("value").toString());
//    			insertComa = false;
//    		}else if(StringUtils.equals(customField.get("name").toString(), "Material")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Swatch Request Form")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Price")) {
////    			strbuild.append(createAttributeJson("price_range", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Related Products")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("More Colors Icon")) {
////    			strbuild.append(createAttributeJson("more_colors_icon", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Recycle Icon")) {
////    			strbuild.append(createAttributeJson("recycle_icon", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Manufacturer Part Number")) {
////    			strbuild.append(createAttributeJson("manufacturer_part_number", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Quick Ship Icon")) {
////    			strbuild.append(createAttributeJson("quick_ship_icon", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Free Shipping Icon")) {
////    			strbuild.append(createAttributeJson("free_shipping_icon", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Lead-Time")) {
//    			strbuild.append(createAttributeJson("lead_time", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Lead-Time")) {
//    			strbuild.append(createAttributeJson("lead_time", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Yahoo Google Shopping ID")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Yahoo Bing Shopping ID")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping MPN")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping GTIN")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping Promotion ID")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping Custom_Label_0")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping Custom_Label_1")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping shipping_label")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Yahoo Google Taxonomy")) {
//    			strbuild.append(createAttributeJson("yahoo_google_taxonomy", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Google Shopping Availability")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping Condition")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Google Shopping Identifer_exists")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Trade Discount")) {
//    			strbuild.append(createAttributeJson("trade_discount", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Sale Flag")) {
////    			strbuild.append(createAttributeJson("sale_flag", null, null, customField.get("value").toString()));
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Highlight 1")) {
//    			strbuild.append(createAttributeJson("highlight_1", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Highlight 2")) {
//    			strbuild.append(createAttributeJson("highlight_2", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Highlight 3")) {
//    			strbuild.append(createAttributeJson("highlight_3", null, null, customField.get("value").toString()));
//    		}else if(customField.get("name").toString().equals("Promo Text")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("PLP Promo Text")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Highlight 4")) {
//    			System.out.println("****************"+i);
//    		}else if(customField.get("name").toString().equals("Assembly Required")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("id")) {
//    			insertComa = false;
//    		}else if(customField.get("name").toString().equals("Category Excerpt")) {
//    			insertComa = false;
//    		}
//    		else {
//    			strbuild.append(createAttributeJson(customField.get("name").toString().toLowerCase(), null, null, customField.get("value").toString()));
//    		}
//    		if(i != newObj.size() - 1 && insertComa) {
//    			strbuild.append(",");
//    		}
//    	}
    	StringBuilder removedComaResp = new StringBuilder();
    	if( strbuild.length() > 0 ) {
    		removedComaResp = removeComaAtEnd(strbuild.toString());
    	}
    	
//    	if(newCategories.size() > 0) {
//    		removedComaResp.append(",");
//    		removedComaResp.append(createAttributeArrayJson("item_type", null, null, newCategories));
//    	}
    	
    	removedComaResp.append("}");
    	if (isOptionProductsExists){
    		removedComaResp.append(",").append(createUpdateOptionProductsAssociation(data));
    	}
    	
    	
    	removedComaResp.append("}");
    	System.out.println(removedComaResp);
    	return removedComaResp.toString();
    }
    
    public String createUpdateOptionProductsAssociation(Map<String, Object> data) {
		List<Map<String, Object>> modifiers = (List<Map<String, Object>>) data.get("modifiers");

		StringBuilder strbuild = new StringBuilder("\"associations\": { \"add_ons_outdoor_furniture\": { ");
		List<String> prodOptions = new ArrayList<>();
		int count = 0;
		for (Map<String, Object> modifierObj: modifiers){ 
			String display_name = (String) modifierObj.get("display_name");
			if (!display_name.equalsIgnoreCase("not_an_option")) {
				List<Map<String, Object>> optionValues = (List<Map<String, Object>>) modifierObj.get("option_values");
				for (Map<String, Object> optionProduct: optionValues){
					String[] optionsSku = optionProduct.get("label").toString().split("--");
					if (optionsSku.length >=2) {
						prodOptions.add(optionsSku[1].trim());
					}
				}
			}
		}	
//		strbuild.append("\"").append(optionsSku[1]).append("\"");
		strbuild.append(createKeyValueListJson("products", prodOptions));
		strbuild.append("}}");
		return strbuild.toString();

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
    
    
//	public String request(Map<String, Object> convertedResp) {
//		RequestUtil requestData = new RequestUtil();
//		String baseResponse =createUpdateBaseProduct(convertedResp);
//		if(StringUtils.isNotEmpty(baseResponse)) {
//			requestData.createUpdateOptionProducts(convertedResp);
//		}
//		
//		return baseResponse;
//	}
//	
    
	public List<String> createUpdateOptionProducts(String sku, Map<String, Object> data, WriteResult result, List<String> createdOptions, Map<String, String> attributes) {
		List<Map<String, Object>> modifiers = (List<Map<String, Object>>) data.get("modifiers");
		List<Map<String, Object>> variants = (List<Map<String, Object>>) data.get("variants");
		
		int MAX_PROD_COUNT = 50;
		List<String> requestList = new ArrayList<>();

		if(modifiers.isEmpty()) {
			result.setModifiers(false);
		}
		
		for (Map<String, Object> variantsrObj: variants){ 
			if(!StringUtils.equals(variantsrObj.get("sku").toString(), sku)) {
				result.setVariants(true);
			}
		}
		
		StringBuilder strbuild = new StringBuilder("");
		int count = 0;
		for (Map<String, Object> modifierObj: modifiers){ 
			String display_name = (String) modifierObj.get("display_name");
			if (!display_name.equalsIgnoreCase("not_an_option")) {
				List<Map<String, Object>> optionValues = (List<Map<String, Object>>) modifierObj.get("option_values");
				if(optionValues.size() > 0) {
					result.setModifiers(true);
				}
				
				for (Map<String, Object> optionProduct: optionValues){
					count++;
					String optProdStr = createOptionProduct(display_name, optionProduct, createdOptions, attributes);
					if (StringUtils.isNotEmpty(optProdStr)) {
						strbuild.append(optProdStr).append("\n");
						if (count == MAX_PROD_COUNT) {
							requestList.add(strbuild.toString());
							strbuild = new StringBuilder("");
							result.incrementOptionsCount(count);
							count =0;
						}
					}
				}
			}
		}	
		requestList.add(strbuild.toString());
		result.incrementOptionsCount(count);
		return requestList;
	}
	
	public List<String> getAllValueOptions(String baseSku, Map<String, Object> data, WriteResult result, Map<String, String> attributes) {
		List<Map<String, Object>> modifiers = (List<Map<String, Object>>) data.get(BuilderConstants.MODIFIERS);
		List<Map<String, Object>> options = (List<Map<String, Object>>) data.get(BuilderConstants.OPTIONS);
		List<Map<String, Object>> variants = (List<Map<String, Object>>) data.get(BuilderConstants.VARIANTS);
		
		int count = 0;
		List<String> optionsList = new ArrayList<>();
		List<String> pricesList = new ArrayList<>();
		getOptionValuesFromParent(modifiers, baseSku, BuilderConstants.MODIFIERS, optionsList, count, result, attributes, pricesList);
		getOptionValuesFromParent(options, baseSku, BuilderConstants.OPTIONS, optionsList, count, result, attributes, pricesList);
		getOptionValuesFromParent(variants, baseSku, BuilderConstants.VARIANTS, optionsList, count, result, attributes, pricesList);
		
		return optionsList;
	}
	
	public List<String> getOptionValuesFromParent(List<Map<String, Object>> movs, String baseSku, String type, List<String> optionsList, int count, WriteResult result, Map<String, String> attributes, List<String> pricesList) {
		StringBuilder strbuild = new StringBuilder("");
		int MAX_PROD_COUNT = 50;
		
		List<String> createdLabels = new ArrayList<String>();
		for(Map<String, Object> data: movs) {
			String displayName = StringUtils.EMPTY;
			if(!type.equals(BuilderConstants.VARIANTS)) {
				displayName = data.get(BuilderConstants.DISPLAY_NAME).toString();
			}
			if(type.equals(BuilderConstants.VARIANTS) && StringUtils.equals(baseSku, data.get(BuilderConstants.SKU).toString())) {
				break;
			}
			if(!displayName.equalsIgnoreCase("not_an_option") && type.equals(BuilderConstants.VARIANTS)) {
				List<Map<String, Object>> options = (List<Map<String, Object>>) data.get(BuilderConstants.OPTION_VALUES);
				for(Map<String, Object>option: options) {
					if(!type.equals(BuilderConstants.VARIANTS) && StringUtils.equals(baseSku, option.get(BuilderConstants.SKU).toString())) {
						break;
					}
					if(type.equals(BuilderConstants.VARIANTS)) {
						displayName = option.get(BuilderConstants.OPTION_DISPLAY_NAME).toString();
					}
					String label = option.get("label").toString();
					if(!createdLabels.contains(label)) {
						count++;
						String response = createOptionProduct(displayName, option, createdLabels, attributes);
						if(!response.isEmpty()) {
							strbuild.append(response).append("\n");
							if (count == MAX_PROD_COUNT) {
								optionsList.add(strbuild.toString());
								strbuild = new StringBuilder("");
								result.incrementOptionsCount(count);
								count = 0;
							}
						}
					}
				}
			}
		}
		optionsList.add(strbuild.toString());
		result.incrementOptionsCount(count);
		return optionsList;
	}
	
	private String createOptionProduct(String displayName, Map<String, Object> data, List<String> createdOptions, Map<String, String> attributes) {
		String label = data.get("label").toString();
		String[] optionsSku = label.split("--");
		String displayCode = attributes.get("display_name"+"-"+displayName);
		StringBuilder strbuild = new StringBuilder("");
		if (optionsSku.length >=2 && !createdOptions.contains(data.get("label").toString())) {
			createdOptions.add(label);
			strbuild.append("{");
	    	strbuild.append(createKeyValueJson("identifier", optionsSku[1].trim())).append(",");
	    	strbuild.append(createKeyValueJson("family", "Accessories_Lighting")).append(",");
	    	strbuild.append("\"values\": {");
	    	strbuild.append(createAttributeJson("sku_type", null, null, "O")).append(",");
	    	strbuild.append(createAttributeJson("display_name", null, null, displayCode));
	//    	strbuild.append(createAttributeJson("label", null, null, optionsSku[0].trim())).append(",");
//	    	strbuild.append(createAttributeImageJson("swatch_file", null, null, "9/1/e/d/91ed04c50887ee44b94bad26c4d4ac7acbd85b28_Sag_Harbor_Dining_Armchair9607__14425.1615580640.1000.1000.jpg", "fileURL"));
	    	
	    	strbuild.append("}");
	    	strbuild.append("}");
		}else {
			System.out.println(label);
		}
		
		return strbuild.toString();
	}
	
	public String createProductPrices(String baseSku, Map<String, Object> data, WriteResult result) {
		
		List<Map<String, Object>> modifiers = (List<Map<String, Object>>) data.get("modifiers");

		StringBuilder strbuild = new StringBuilder("");
		int count = 0;
		for (Map<String, Object> modifierObj: modifiers){ 
				List<Map<String, Object>> optionValues = (List<Map<String, Object>>) modifierObj.get("option_values");
				for (Map<String, Object> optionProduct: optionValues){
					String[] optionsSku = optionProduct.get("label").toString().split("--");
//					if (optionsSku.length >=2) { //removed - Need to consider all options with prices even if there is no SKU
						Matcher positivePriceRegex = Pattern.compile("\\((\\+\\$.*?)\\)").matcher(optionProduct.get("label").toString());
						Matcher negativePriceRegex = Pattern.compile("\\((\\-\\$.*?)\\)").matcher(optionProduct.get("label").toString());
						String price = null;
						while (positivePriceRegex.find()) {
							price = positivePriceRegex.group(1);
							price = price.replaceAll("[+$]*", "");
						}
						
						if(price == null) {
							while (negativePriceRegex.find()) {
								price = negativePriceRegex.group(1);
								price = price.replaceAll("[-$]*", "");
							}
						}
						if(price!=null) {
							count++;
						}
						String priceResp = createProductPrice(baseSku, optionProduct);
						
						if(priceResp!=null) {
							strbuild.append(priceResp).append("\n");
						}
//					}
				}
			
		}
		result.setPriceCount(count);
		return strbuild.toString();
	}
	
	private String createProductPrice(String baseSku, Map<String, Object> data) {
		StringBuilder strbuild = null;
		String label = data.get("label").toString();
		
		Matcher positivePriceRegex = Pattern.compile("\\((\\+\\$.*?)\\)").matcher(label);
		Matcher negativePriceRegex = Pattern.compile("\\((\\-\\$.*?)\\)").matcher(label);
		String price = null;
		
		while (positivePriceRegex.find()) {
			price = positivePriceRegex.group(1);
			price = price.replaceAll("[+$]*", "");
		}
		
		if(price == null) {
			while (negativePriceRegex.find()) {
				price = negativePriceRegex.group(1);
				price = price.replaceAll("[-$]*", "");
			}
		}
		
		String[] optionsSku = data.get("label").toString().split("--");
		
		if(data.get("label").toString().equals("Add Protective Cover FC003 (+$90.00)")) {
			System.out.println(data.get("label").toString());
		}
		
		if(price!=null) {
			String[] gradeFinder = label.split(" ");
			String identifier2 = null;
			strbuild = new StringBuilder("{");
			if(Arrays.asList(gradeFinder).contains("Grade") && gradeFinder.length > 0) {
				identifier2 = gradeFinder[1];
			}else if(optionsSku.length >= 2) {
				identifier2 = optionsSku[1];
			}else {
				Date date = new Date();
				identifier2 = date.toString();
			}
	    	strbuild.append(createKeyValueJson("identifier", baseSku+"_"+identifier2)).append(",");
	    	strbuild.append("\"values\": {");
	    	strbuild.append(createAttributeJson("sku_type", null, null, "P")).append(",");
    		strbuild.append(createAttributeJson("retail_price", null, null, price)).append(",");
	    	strbuild.append(createAttributeJson("family", null, null, "product_pricing")).append(",");
	    	
	    	if(Arrays.asList(gradeFinder).contains("Grade") && gradeFinder.length > 0) {
	    		strbuild.append(createAttributeJson("grade", null, null, gradeFinder[1])).append(",");
	    	}
	    	strbuild.append(createAttributeJson("base_sku", null, null, baseSku)).append(",");
	    	strbuild.append(createAttributeJson("option_sku", null, null, (optionsSku.length < 2)?identifier2:optionsSku[1]));
	    	strbuild.append("}");
	    	strbuild.append("}");
		}
		return (strbuild!=null)?strbuild.toString():null;
	}
}
