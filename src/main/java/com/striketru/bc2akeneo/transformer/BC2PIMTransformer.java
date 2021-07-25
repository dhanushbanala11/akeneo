package com.striketru.bc2akeneo.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.constants.Constants;
import com.striketru.bc2akeneo.model.PIMValue;
import com.striketru.bc2akeneo.reader.ReaderData;
import com.striketru.bc2akeneo.util.CSVUtil;
import com.striketru.bc2akeneo.util.CSVUtil.CSV_FILE_TYPE;
import com.striketru.bc2akeneo.writer.WriterData;
import com.striketru.conn.base.Transformer;
import com.striketru.pim.model.AttributeJson;
import com.striketru.pim.model.ImageJson;
import com.striketru.pim.model.ProductJson;

public class BC2PIMTransformer extends Transformer<ReaderData, WriterData> {
	
    private static final Logger LOGGER = LogManager.getLogger(BC2PIMTransformer.class);

    enum OPTIONS{MODIFIERS, OPTIONS, VARIANTS}
	
	private CSVUtil csvUtil = new CSVUtil();
	private Map<String, String> optionAttributes;
	private Map<String, String> families;
	private Map<String, PIMValue> customFields;
	

	public BC2PIMTransformer(){
		this.optionAttributes = csvUtil.getPropertyFromCSV(Constants.ATTRIBUTES_CSV, CSV_FILE_TYPE.ATTRIBUTE_OPTION);
		this.families = csvUtil.getPropertyFromCSV(Constants.FAMILIES_CSV, CSV_FILE_TYPE.FAMILIES);
		this.customFields = csvUtil.getPropertyFromCSV(Constants.CUSTOM_FIELDS);

	}
	
	@Override
	public WriterData execute(ReaderData readerData) {
		WriterData writerData = new WriterData(getStringDataFromMap(readerData.getProduct(),"sku")); 
		try {
			String family = getFamilyCode(readerData.getProduct());
			processBaseProduct(readerData.getProduct(), family, writerData);
			processOptionProduct(readerData.getProduct(), Constants.MODIFIERS, writerData);
			processOptionProduct(readerData.getProduct(), Constants.OPTIONS, writerData);
			processOptionProduct(readerData.getProduct(), Constants.VARIANTS, writerData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return writerData;
	}

    public void processBaseProduct(Map<String, Object> data, String family, WriterData writerData){
    	writerData.setBaseProduct(new ProductJson());
    	ProductJson prodJson = writerData.getBaseProduct();
    	prodJson.setIdentifier(getStringDataFromMap(data,"sku"));
    	prodJson.setFamily(family);
    	prodJson.setCategories((List<Object>) data.get("categories"));
    	
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "B"));
    	String availability = "available".equals(getStringDataFromMap(data, "availability"))? "true": "false";
    	prodJson.addAttributeValues(new AttributeJson("availability", null, null, availability, false));
    	prodJson.addAttributeValues(new AttributeJson("depth", null, null, getStringDataFromMap(data, "depth"), "INCH", null));
    	prodJson.addAttributeValues(new AttributeJson("product_description", null, null, getStringDataFromMap(data, "description")));
    	String gtin = getStringDataFromMap(data, "gtin");
    	if (StringUtils.isNotEmpty(gtin) && gtin.indexOf("AT") >= 0 ) { 
    		String newgtin = gtin.substring(gtin.indexOf("AT") + 2);
        	prodJson.addAttributeValues(new AttributeJson("gtin", null, null, newgtin));
    	}
    	prodJson.addAttributeValues(new AttributeJson("height", null, null, getStringDataFromMap(data, "height"), "INCH", null));
    	prodJson.addAttributeValues(new AttributeJson("bigcommerce_id", null, null, getStringDataFromMap(data, "id")));
    	prodJson.addAttributeValues(new AttributeJson("is_price_hidden", null, null, getStringDataFromMap(data, "is_price_hidden"), false));
    	prodJson.addAttributeValues(new AttributeJson("is_visible", null, null, getStringDataFromMap(data, "is_visible"), false));
    	prodJson.addAttributeValues(new AttributeJson("sale_price", null, null, getStringDataFromMap(data, "map_price")));
    	prodJson.addAttributeValues(new AttributeJson("meta_description", null, null, getStringDataFromMap(data, "meta_description")));
    	prodJson.addAttributeValues(new AttributeJson("manufacturer_part_number", null, null, getStringDataFromMap(data, "mpn")));
    	prodJson.addAttributeValues(new AttributeJson("product_title", null, null, getStringDataFromMap(data, "name")));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_description", null, null, getStringDataFromMap(data, "open_graph_description")));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_title", null, null, getStringDataFromMap(data, "open_graph_title")));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_type", null, null, getStringDataFromMap(data, "open_graph_type")));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_use_image", null, null, getStringDataFromMap(data, "open_graph_use_image"), false));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_use_meta_description", null, null, getStringDataFromMap(data, "open_graph_use_meta_description"), false));
    	prodJson.addAttributeValues(new AttributeJson("open_graph_use_product_name", null, null, getStringDataFromMap(data, "open_graph_use_product_name"), false));
    	prodJson.addAttributeValues(new AttributeJson("page_title", null, null, getStringDataFromMap(data, "page_title")));
    	prodJson.addAttributeValues(new AttributeJson("price_hidden_label", null, null, getStringDataFromMap(data, "price_hidden_label")));
    	prodJson.addAttributeValues(new AttributeJson("retail_price", null, null, getStringDataFromMap(data, "retail_price")));
    	prodJson.addAttributeValues(new AttributeJson("search_keywords", null, null, getStringDataFromMap(data, "search_keywords")));
    	prodJson.addAttributeValues(new AttributeJson("sort_order", null, null, getStringDataFromMap(data, "sort_order")));
    	prodJson.addAttributeValues(new AttributeJson("tax_class_id", null, null, getStringDataFromMap(data, "tax_class_id")));
    	prodJson.addAttributeValues(new AttributeJson("upc", null, null, getStringDataFromMap(data, "upc")));
    	prodJson.addAttributeValues(new AttributeJson("warranty", null, null, getStringDataFromMap(data, "warranty")));
    	prodJson.addAttributeValues(new AttributeJson("weight", null, null, getStringDataFromMap(data, "weight"), "POUND", null));
    	prodJson.addAttributeValues(new AttributeJson("width", null, null, getStringDataFromMap(data, "width"), "INCH", null));

    	
    	prodJson.addAttributeValues(new AttributeJson("option_set_id", null, null, getStringDataFromMap(data, "option_set_id")));
    	prodJson.addAttributeValues(new AttributeJson("option_set_display", null, null, getStringDataFromMap(data, "option_set_display")));
    	prodJson.addAttributeValues(new AttributeJson("product_video_url", null, null, getStringDataFromMap(data, "product_video_url")));
    	prodJson.addAttributeValues(new AttributeJson("sustainable", null, null, getStringDataFromMap(data, "sustainable"), false));
    	prodJson.addAttributeValues(new AttributeJson("made_in_the_usa", null, null, getStringDataFromMap(data, "made_in_the_usa"), false));
    	prodJson.addAttributeValues(new AttributeJson("stacking", null, null, getStringDataFromMap(data, "stacking"), false));
    	prodJson.addAttributeValues(new AttributeJson("rebate", null, null, getStringDataFromMap(data, "rebate"), false));
    	
    	List<Object> bcCustomFields  =  (List<Object>) data.get("custom_fields");
    	Set<String> processedCustomField = new HashSet<>();
    	if(bcCustomFields != null) {
	    	for (Object obj : bcCustomFields) {
	    		Map<String, String> field = (Map<String, String>)obj;
	    		PIMValue pimValue = customFields.get(field.get("name"));
	    		if (field.get("value")!=null && pimValue != null && !processedCustomField.contains(field.get("name"))) {
		    		AttributeJson attribute = getValueJson(pimValue, field.get("value"));
		    		if (attribute != null) { 
			    		processedCustomField.add(field.get("name"));
			    		prodJson.addAttributeValues(attribute);	
		    		}
	    		}
	    	}
	    	for (String key:getMultiSelectData().keySet()) {
	    		prodJson.addAttributeValues(new AttributeJson(key, null, null, new ArrayList<>(getMultiSelectData().get(key))));
	    	}
    	}
    	
    	List<Map<String, Object>> imageList = (List<Map<String, Object>>) data.get(Constants.IMAGES);
    	if (imageList != null) { 
			for(Map<String, Object> imageInfo: imageList) {
				String orderNo = getStringDataFromMap(imageInfo, "sort_order");
				String identifier = "";
				if (orderNo.equals("0")) {
					identifier = "primary_image_description";
				} else {
					identifier = "image_description_"+orderNo;
				}
				writerData.getBaseProduct().addAttributeValues(new AttributeJson(identifier, null, null, getStringDataFromMap(imageInfo, "description")));
				processImages(prodJson.getIdentifier(), getStringDataFromMap(imageInfo, "url_standard"), orderNo, writerData);
			}
    	}
    }
	/**
	 * 
	 * @param data
	 * @param type
	 * @param writerData
	 */
	public void processOptionProduct(Map<String, Object> data, String type, WriterData writerData){
		List<Map<String, Object>> optionsBCList = null;
		String displayName = null;
		if (StringUtils.equalsIgnoreCase(type, Constants.MODIFIERS)) {
			optionsBCList = (List<Map<String, Object>>) data.get(Constants.MODIFIERS);
		} else if (StringUtils.equalsIgnoreCase(type, Constants.OPTIONS)) {
			optionsBCList = (List<Map<String, Object>>) data.get(Constants.OPTIONS);
		} else if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS)) {
			optionsBCList = (List<Map<String, Object>>) data.get(Constants.VARIANTS);
		}
		int count = 0;
		if (optionsBCList != null) { 
			for(Map<String, Object> optionsBC: optionsBCList) {
				if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS) && isVariantNotBaseSku(optionsBC, writerData.getSku())) {
					continue;
				} else {
					displayName = getStringDataFromMap(optionsBC, Constants.DISPLAY_NAME);
				}
				if(!StringUtils.equalsIgnoreCase(displayName, "not_an_option")) {
					List<Map<String, Object>> optionValues = (List<Map<String, Object>>) optionsBC.get(Constants.OPTION_VALUES);
					for(Map<String, Object> optionVal: optionValues) {
						String label = optionVal.get("label").toString();
						String[] optionsSku = getSKU(getStringDataFromMap(optionVal, "label"));
						if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS)) {
							displayName = optionVal.get(Constants.OPTION_DISPLAY_NAME).toString();
						}
						if (StringUtils.equalsIgnoreCase(type, Constants.MODIFIERS)) {
							// Calling the price 
							String price = getPriceFromLabel(label);
							if (price != null) {
								writerData.addPrice(createProductPrice(optionVal, writerData.getSku(), label, price));	
							}
							if (optionVal.get("value_data") != null) {
								processImages(optionsSku[1].trim(), getStringDataFromMap((Map<String, Object>)optionVal.get("value_data"), "image_url"), "-1", writerData);
							}
						}
						if (writerData.getOptionsProduct().get(label) == null) {
							writerData.getOptionsProduct().put(label, createOptionProduct(optionVal, optionsSku, displayName));
						}
						count++;
					}
				}
			}
			writerData.getReport().addCount(type, count);
		}
    }

    private ProductJson createOptionProduct(Map<String, Object> data, String[] optionsSku, String displayName) {
    	ProductJson prodJson = new ProductJson();
    	String displayCode = optionAttributes.get("display_name"+"-"+displayName);
    	if (optionsSku.length >=2 ) {
	    	prodJson.setIdentifier(optionsSku[1].trim());
	    	prodJson.setFamily(getFamilyCodeOptions(data));
	    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "O"));
	    	prodJson.addAttributeValues(new AttributeJson("display_name", null, null, displayCode));
	    	if(prodJson.getFamily().equals("options_swatches")) {
		    	prodJson.addAttributeValues(new AttributeJson("marketing_color", null, null, optionsSku[0].trim()));
	    	} else {
		    	prodJson.addAttributeValues(new AttributeJson("option_value", null, null, optionsSku[0].trim()));
	    	}

    	}
    	return prodJson;
    }
    
    private ProductJson createProductPrice( Map<String, Object> data, String baseSKU, String label, String price) {
		String[] gradeFinder = label.split(" ");
		String[] optionsSku = label.split("--");
		String id = null;
		if(Arrays.asList(gradeFinder).contains("Grade") && gradeFinder.length > 0) {
			id = gradeFinder[1];
		}else if(label.contains("--")) {
			id = optionsSku[1];
		}else {
			id = getNextId();
		}
    	ProductJson prodJson = new ProductJson();
    	prodJson.setIdentifier(baseSKU+"_"+id.trim());
    	prodJson.setFamily("product_pricing");
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "P"));
    	if (price.indexOf("$") >0) {
    		price = price.substring(price.indexOf("$")+1);
    	}
    	prodJson.addAttributeValues(new AttributeJson("retail_price", null, null, price));
    	prodJson.addAttributeValues(new AttributeJson("base_sku", null, null, baseSKU));
    	prodJson.addAttributeValues(new AttributeJson("option_sku", null, null, (optionsSku.length < 2)?id:optionsSku[1]));
    	if(Arrays.asList(gradeFinder).contains("Grade") && gradeFinder.length > 0) {
    		prodJson.addAttributeValues(new AttributeJson("grade", null, null, gradeFinder[1]));
    	}
    	return prodJson;
    }
	
    private void processImages(String sku, String url, String sort_order, WriterData writerData) {
    	if (StringUtils.isNotEmpty(url)) { 
    		String attributeCode = null;
			if (sort_order.equals("-1")) { 
				attributeCode = "swatch_file";
			} else if (sort_order.equals("0")) { 
				attributeCode = "primary_image";
			} else {
				attributeCode = "image_"+sort_order;
			}
	    	writerData.getImages().add(new ImageJson(sku, url, attributeCode, null, null));
    	}
    }
    
	public AttributeJson getValueJson(PIMValue pimvalue, String data) {
		if (pimvalue.isTextArea() || pimvalue.isText()) {
			return new AttributeJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isNumber()) {
			return new AttributeJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isMetric()) {
			return new AttributeJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isBoolean()) {
			if (data.equalsIgnoreCase("yes")) {
				return new AttributeJson(pimvalue.getCode(), null, null, "true", false);	
			} else {
				return new AttributeJson(pimvalue.getCode(), null, null, "false", false);	
			}
		} else if (pimvalue.isMultiSelect()) {
			String newcode = pimvalue.getCode().trim()+"-"+data.trim();
			addMultiSelectData(pimvalue.getCode(), optionAttributes.get(newcode));
			return null;
		} else if (pimvalue.isSimpleSelect()) {
			String code = optionAttributes.get(pimvalue.getCode().trim()+"-"+data.trim());
			if(code != null) {
				return new AttributeJson(pimvalue.getCode(), null, null, code);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}


	public boolean isVariantNotBaseSku(Map<String, Object> data, String baseSKU) {
		String varSKU = getStringDataFromMap(data, "sku");
		return varSKU.equalsIgnoreCase(baseSKU);
	}
	
    public boolean isOptionProductsNotExists(Map<String, String> optionMap, String key) {
    	if(optionMap == null) {
    		optionMap = new HashMap<>();
    	}
    	return optionMap.get(key)==null ? true : false;
    }
    
    public String getPriceFromLabel(String label) {
    	String priceString = null;
    	if (StringUtils.isNotEmpty(label) && label.indexOf("(") >0 && label.indexOf(")") > 0 ) {
    		priceString = label.substring(label.indexOf("(") +1, label.indexOf(")"));
    	}
    	return priceString;
    }
    
	public String getFamilyCode(Map<String, Object> data) {
		List<Integer> categories = (List<Integer>) data.get("categories");	
		if (categories!= null && categories.size() >2) {
			for (Integer code: categories) {
				if ( families.get(String.valueOf(code)) != null) {
					return families.get(String.valueOf(code));
				}
			}
		}
		return "no_family";
	}
	
	public String getFamilyCodeOptions(Map<String, Object> data) {
    	String family = getStringDataFromMap(data, "type");
    	if(StringUtils.equalsIgnoreCase(family, "dropdown")) {
    		family = "options_dropdown";
    	}else {
    		family = "options_swatches";
    	}		
		return family;
	}
	
	public String[] getSKU(String label) {
		String[] optionsSku = new String[2];
		int index = label.indexOf("--"); 
		if (index > -1){
			optionsSku[0] = label.substring(0, index);
			optionsSku[1] = label.substring(index+2);
		} else {
			optionsSku[0] = label;
			optionsSku[1] = getNextId();
		}
		return optionsSku;
	}
}
