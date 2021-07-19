package com.striketru.bc2akeneo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.striketru.bc2akeneo.constants.BuilderConstants;
import com.striketru.bc2akeneo.model.PIMValue;
import com.striketru.bc2akeneo.model.WriteResult;

public class RequestUtil {
	public static Gson gson = new Gson();
	private static final String[] ignoreList = {"Swatch Request Form","Google Shopping Availability","Google Shopping Condition","Google Shopping Identifer exists","Features","Google Shopping Promotion ID","Width Range","Width Unit","Height Unit","Depth Range","Depth Unit","Seath Height Unit","Weight Unit","Descriptor","Base Variant Id","Bin Picking Number","Bulk Pricing Rules","Calculated Price","Categories","Condition","Cost Price","Product Resources Shipping","Seating Width ( in.)","Seating Depth ( in.)","Seating Height ( in.)","Seating Seat Height ( in.)","Seating Arm Height ( in.)","Seating Weight (lbs.)","Seating Material","Dimensions ( in.)","Heat Output/BTUs","Any Other Relevant Info","Gift Wrapping Options List","Gift Wrapping Options Type","Height Range","Url Standard 1","Url Standard 2","Inventory Level","Inventory Tracking","Inventory Warning Level","Is Condition Shown","Is Featured","Is Free Shipping","Is Preorder Only","Layout File","Map Price","Order Quantity Maximum","Order Quantity Minimum","Preorder Message","Preorder Release Date","Primary Image File","Product Tax Code","Reviews Count","Reviews Rating Sum","Total Sold","Type","View Count","Seating Width Range","Umbrellas Width Range","Grills Width Range","Heaters Width Range","Seating Height Range","Umbrellas Height Range","Grills Height Range","Heaters Height Range","Fire Pits Height Range","Seating Depth Range","Umbrellas Depth Range","Grills Depth Range","Heaters Depth Range","Sets","Option Type","Featured Highlight","Specs Highlight","Lead-Time 2","Blacklist", "Brand","Cushions", "Height", "Width","Depth", "Use"};
	private static long customSKU = 1000000;
	private Map<String, String> optionAttributes = null;
	private Map<String, PIMValue> customFields = null;
	
    public Map<String, String> getOptionAttributes() {
		return optionAttributes;
	}


	public void setOptionAttributes(Map<String, String> optionAttributes) {
		this.optionAttributes = optionAttributes;
	}


	public Map<String, PIMValue> getCustomFields() {
		return customFields;
	}


	public void setCustomFields(Map<String, PIMValue> customFields) {
		this.customFields = customFields;
	}

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
    
    private static final String PIM_ATTR_BOOLEAN_JSON_PATTERN = "\"%s\":[{\"locale\":%s,\"scope\":%s, \"data\": %s}]";
    public static String createdAttributeBooleanJson(String key, String locale, String scope, String data) {
    	data = StringEscapeUtils.escapeHtml4(data);
    	return String.format(PIM_ATTR_BOOLEAN_JSON_PATTERN, key, getValue(locale), getValue(scope), Boolean.valueOf(data));
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
    

    public String createUpdateBaseProduct(Map<String, Object> data, String family,  boolean isOptionProductsExists, Map<String, String> optionAttributes2){
    	List<String> categories = (List<String>) data.get("categories");

    	String step1 = StringUtils.join(categories, "\", \"");// Join with ", "
    	String step2 = StringUtils.wrap(step1, "\"");// Wrap step1 with "
    	
    	Map<String, Object> primaryImage = (Map<String, Object>) data.get("primary_image"); 
    	System.out.println(data.get("name"));
    	StringBuilder strbuild = new StringBuilder("{");
//    	strbuild.append(createKeyValueJson("product_title", data.get("name").toString())).append(",");
    	
    	strbuild.append(createKeyValueJson("family", family));
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
    	
    	List<Object> bcCustomFields  =  (List<Object>) data.get("custom_fields");
    	for (Object obj : bcCustomFields) {
    		Map<String, String> field = (Map<String, String>)obj;
    		PIMValue pimValue = customFields.get(field.get("name"));
    		if (pimValue != null) {
    			strbuild.append(",").append(getValueJson(pimValue, field.get("value"), optionAttributes));
    		}
    	}
    	
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
    
	public List<String> getAllValueOptions(String baseSku, Map<String, Object> data, String family, WriteResult result, List<String> optionProductRequest, List<String> priceProductRequest) {
		List<Map<String, Object>> modifiers = (List<Map<String, Object>>) data.get(BuilderConstants.MODIFIERS);
		List<Map<String, Object>> options = (List<Map<String, Object>>) data.get(BuilderConstants.OPTIONS);
		List<Map<String, Object>> variants = (List<Map<String, Object>>) data.get(BuilderConstants.VARIANTS);
		
		Map<String, String> optionMap = new HashMap<>();
		getOptionValuesFromParent(modifiers, baseSku, BuilderConstants.MODIFIERS, family, result, priceProductRequest, optionMap);
		getOptionValuesFromParent(options, baseSku, BuilderConstants.OPTIONS, family, result, priceProductRequest, optionMap);
		getOptionValuesFromParent(variants, baseSku, BuilderConstants.VARIANTS, family, result, priceProductRequest, optionMap);
		return getRequestList(optionMap);
	}
	
	public void getOptionValuesFromParent(List<Map<String, Object>> movs, String baseSku, String type, String family, WriteResult result, List<String> pricesList, Map<String, String> optionMap) {
		StringBuilder strbuild = new StringBuilder("");
//		List<String> createdLabels = new ArrayList<String>();
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
					if(checkOptionProductsNotExists(optionMap,label)) {
						optionMap.put(label, createOptionProduct(displayName, option, family, optionMap));
						result.incrementOptionsCount();
					}
				}
			}
		}
//		optionsList.add(strbuild.toString());
//		result.incrementOptionsCount(count);
//		return optionsList;
	}
	
	private String createOptionProduct(String displayName, Map<String, Object> data, String family, Map<String, String> optionMap) {
		String label = data.get("label").toString();
		String[] optionsSku = getSKU(getStringFromMap(data, "label"));
		String displayCode = optionAttributes.get("display_name"+"-"+displayName);
		StringBuilder strbuild = new StringBuilder("");
		if (optionsSku.length >=2 ) {
			strbuild.append("{");
	    	strbuild.append(createKeyValueJson("identifier", optionsSku[1].trim())).append(",");
	    	strbuild.append(createKeyValueJson("family", family)).append(",");
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
	
	public String getStringFromMap(Map<String, Object> data, String key) {
		if (data != null && data.get(key) != null) {
			return data.get(key).toString();
		}
		return null;
	}

	public List<Map<String, Object>> getListOfMapFromMap(Map<String, Object> data, String key) {
		if (data != null && data.get(key) != null) {
			return (List<Map<String, Object>>) data.get(key);
		}
		return new ArrayList<>();
	}

	public String getValueJson(PIMValue pimvalue, String data, Map<String, String> optionAttributes) {
		if (pimvalue.isTextArea() || pimvalue.isText()) {
			return createAttributeJson(pimvalue.getCode(), null, null, data);
//		} else if (pimvalue.isNumber()) {
//			return createAttributeJson(pimvalue.getCode(), null, null, data);
//		} else if (pimvalue.isMetric()) {
//			return createAttributeJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isBoolean()) {
			return createdAttributeBooleanJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isMultiSelect()) {
			String key = pimvalue.getCode().trim()+"-"+data.trim();
			ArrayList<String> newList = new ArrayList<String>();
			newList.add(optionAttributes.get(key));
			return createAttributeArrayJson(pimvalue.getCode(), null, null, newList);
		} else if (pimvalue.isSimpleSelect()) {
			String key = pimvalue.getCode().trim()+"-"+data.trim();
			return createAttributeJson(pimvalue.getCode(), null, null, optionAttributes.get(key));
//		} else if (pimvalue.isImage()) {
//			return createAttributeJson(pimvalue.getCode(), null, null, data);
//		} else if (pimvalue.isFile()) {
//			return createAttributeJson(pimvalue.getCode(), null, null, data);
		} else {
			return "";
		}
	}
	
	
	
	public String[] getSKU(String label) {
		String[] optionsSku = label.split("--");
		if (optionsSku.length <2 ) {
//			optionsSku = String[2];
			optionsSku = new String[] {new String(optionsSku[0].trim()), String.valueOf(customSKU)};
		}
		return optionsSku;
	}
	
	public List<String> getRequestList(Map<String, String> optionMap) {
		int MAX_PROD_COUNT = 50;
		int count = 0;
		List<String> optionsList = new ArrayList<>();
		StringBuilder strbuild = new StringBuilder("");
		for (String key :  optionMap.keySet()) {
			if (count == MAX_PROD_COUNT) {
				optionsList.add(strbuild.toString());
				strbuild = new StringBuilder("");
				count = 0;
			}
			strbuild.append(optionMap.get(key)).append("\n");
			count++;
		}
		return optionsList;
	}
	
    public boolean checkOptionProductsNotExists(Map<String, String> optionMap, String key) {
    	if(optionMap == null) {
    		optionMap = new HashMap<>();
    	}
    	return optionMap.get(key)==null ? true : false;
    }
}
