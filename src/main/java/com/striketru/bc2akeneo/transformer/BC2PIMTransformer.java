package com.striketru.bc2akeneo.transformer;

import java.util.ArrayList;
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
import com.striketru.pim.model.MediaJson;
import com.striketru.pim.model.ProductJson;

public class BC2PIMTransformer extends Transformer<ReaderData, WriterData> {
	
    private static final Logger LOGGER = LogManager.getLogger(BC2PIMTransformer.class);

    enum OPTIONS{MODIFIERS, OPTIONS, VARIANTS}
   
	private CSVUtil csvUtil = new CSVUtil();
	private Map<String, String> optionAttributes;
	private Map<String, String> families;
	private Map<String, PIMValue> customFields;
	private WriterData writerData; 
	private DescriptionTransformer descripTransformer;
	
	private Map<String, Set<String>> multiValueTextArea;
	private Set<String> priceSkus;
	
	public BC2PIMTransformer(){
		this.optionAttributes = csvUtil.getPropertyFromCSV(Constants.ATTRIBUTES_CSV, CSV_FILE_TYPE.ATTRIBUTE_OPTION);
		this.families = csvUtil.getPropertyFromCSV(Constants.FAMILIES_CSV, CSV_FILE_TYPE.FAMILIES);
		this.customFields = csvUtil.getPropertyFromCSV(Constants.CUSTOM_FIELDS);

	}
	
	@Override
	public WriterData execute(ReaderData readerData) {
		writerData = new WriterData(getStringDataFromMap(readerData.getProduct(),"sku"));
		descripTransformer = new DescriptionTransformer(getStringDataFromMap(readerData.getProduct(),"description"));
		multiValueTextArea = new HashMap<>();
		priceSkus = new HashSet<>();
		setMultiSelectData(new HashMap<>());
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
    	prodJson.addAttributeValues(new AttributeJson("product_description", null, null, descripTransformer.getProductDescription()));
    	prodJson.addAttributeValues(new AttributeJson("care_info", null, null, descripTransformer.getCareInfo()));
    	prodJson.addAttributeValues(new AttributeJson("specs_info", null, null, descripTransformer.getSpecsInfo()));
    	prodJson.addAttributeValues(new AttributeJson("shipping_info", null, null, descripTransformer.getShipInfo()));
    	prodJson.addAttributeValues(new AttributeJson("pdf_info", null, null, descripTransformer.getPdfInfo()));
    	
    	
    	String gtin = getStringDataFromMap(data, "gtin");
    	if (StringUtils.isNotEmpty(gtin) && gtin.indexOf("AT") >= 0 ) { 
    		String newgtin = gtin.substring(gtin.indexOf("AT") + 2);
        	prodJson.addAttributeValues(new AttributeJson("gtin", null, null, newgtin));
    	}
    	prodJson.addAttributeValues(new AttributeJson("height", null, null, getStringDataFromMap(data, "height"), "INCH", null));
    	prodJson.addAttributeValues(new AttributeJson("bigcommerce_id", null, null, getStringDataFromMap(data, "id")));
    	prodJson.addAttributeValues(new AttributeJson("is_price_hidden", null, null, getStringDataFromMap(data, "is_price_hidden"), false));
    	prodJson.addAttributeValues(new AttributeJson("is_visible", null, null, getStringDataFromMap(data, "is_visible"), false));
    	prodJson.addAttributeValues(new AttributeJson("sale_price", null, null, getStringDataFromMap(data, "sale_price")));
    	prodJson.addAttributeValues(new AttributeJson("meta_description", null, null, getStringDataFromMap(data, "meta_description")));
//    	prodJson.addAttributeValues(new AttributeJson("manufacturer_part_number", null, null, getStringDataFromMap(data, "mpn")));
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
    	prodJson.addAttributeValues(new AttributeJson("price", null, null, getStringDataFromMap(data, "price")));
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
	    		PIMValue pimValue = customFields.get(field.get("name").toLowerCase());
	    		if (field.get("value")!=null && pimValue != null && !processedCustomField.contains(field.get("name"))) {
		    		AttributeJson attribute = getAttributeJson(pimValue, field.get("value"));
		    		if (attribute != null) { 
			    		processedCustomField.add(field.get("name"));
			    		prodJson.addAttributeValues(attribute);	
		    		}
	    		}
	    	}
	    	for (String key:getMultiSelectData().keySet()) {
	    		prodJson.addAttributeValues(new AttributeJson(key, null, null, new ArrayList<>(getMultiSelectData().get(key))));
	    	}
	    	for (String key: multiValueTextArea.keySet()) {
	    		StringBuilder strBuild = new StringBuilder();
	    		for(String value: multiValueTextArea.get(key)) {
	    			strBuild.append(value).append("|");
	    		}
	    		prodJson.addAttributeValues(new AttributeJson(key, null, null, strBuild.substring(0, strBuild.lastIndexOf("|"))));
	    	}
    	}
    	List<Map<String, Object>> videoList = (List<Map<String, Object>>) data.get(Constants.VIDEOS);
    	if (videoList != null) { 
    		int count =1;
			for(Map<String, Object> videoInfo: videoList) {
				String videoUrl = Constants.VIDEO_URL + getStringDataFromMap(videoInfo, "video_id");
				if (count == 1) { 
					prodJson.addAttributeValues(new AttributeJson("product_video_url", null, null, videoUrl));
				} else if (count <= Constants.VIDEO_MAX_SUFFIX) {
					prodJson.addAttributeValues(new AttributeJson("product_video_url" + count, null, null, videoUrl));
				} 
				count++;
			}
    	}
    	
    	
    	List<Map<String, Object>> imageList = (List<Map<String, Object>>) data.get(Constants.IMAGES);
    	if (imageList != null) { 
			for(Map<String, Object> imageInfo: imageList) {
				String orderNo = getStringDataFromMap(imageInfo, "sort_order");
				String identifier = "";
				if (Integer.valueOf(orderNo) <= Constants.IMAGE_MAX_SUFFIX) {
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
    	processDocuments(prodJson.getIdentifier(), writerData);
    }
	/**
	 * 
	 * @param data
	 * @param type
	 * @param writerData
	 */
	public void processOptionProduct(Map<String, Object> data, String type, WriterData writerData){
		List<Map<String, Object>> optionsBCList = null;
		
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
				String displayName = null;
				if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS) && isVariantNotBaseSku(optionsBC, writerData.getSku())) {
					continue;
				} else {
					displayName = getStringDataFromMap(optionsBC, Constants.DISPLAY_NAME);
				}
				String optionType = getStringDataFromMap(optionsBC, "type");

				if(!StringUtils.equalsIgnoreCase(displayName, "not_an_option")) {
					List<Map<String, Object>> optionValues = (List<Map<String, Object>>) optionsBC.get(Constants.OPTION_VALUES);
					for(Map<String, Object> optionVal: optionValues) {
						String label = getStringDataFromMap(optionVal, "label");
						// Calling the price 
						String[] labelInfo = parseLabel(label, getStringDataFromMap(optionVal, "id"));
						String price = getPriceFromLabel(label);
						if (price != null ) {
							String identifier = writerData.getSku()+"_"+labelInfo[3].trim();
					    	if (StringUtils.isNotEmpty(labelInfo[0])) {
					    		identifier = writerData.getSku()+"_"+labelInfo[0].trim();
					    	}
					    	if (!priceSkus.contains(identifier)) {
					    		writerData.addPrice(createProductPrice(optionVal, writerData.getSku(), identifier, labelInfo, price));
					    		priceSkus.add(identifier);
					    	}
						}
						if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS)) {
							displayName = optionVal.get(Constants.OPTION_DISPLAY_NAME).toString();
						}
						if (StringUtils.equalsIgnoreCase(type, Constants.MODIFIERS) || StringUtils.equalsIgnoreCase(type, Constants.OPTIONS)) {
							if (optionVal.get("value_data") != null) {
								processImages(labelInfo[3].trim(), getStringDataFromMap((Map<String, Object>)optionVal.get("value_data"), "image_url"), "-1", writerData);
							}
						}
						if (writerData.getOptionsProduct().get(label) == null) {
							writerData.getOptionsProduct().put(label, createOptionProduct(optionVal, labelInfo, displayName, optionType));
						}
						count++;
					}
				}
			}
			writerData.getReport().addCount(type, count);
		}
    }

    private ProductJson createOptionProduct(Map<String, Object> data, String[] labelInfo, String displayName, String optionType) {
    	ProductJson prodJson = new ProductJson();
    	String displayCode = getAttributeCode("display_name", "display_name"+"-"+displayName);
    	prodJson.setIdentifier(labelInfo[3].trim());
    	String family  = "options_swatches";
    	if(StringUtils.equalsIgnoreCase(optionType, "dropdown")) {
    		family = "options_dropdown";
    	}		
    	prodJson.setFamily(family);
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "O"));
    	prodJson.addAttributeValues(new AttributeJson("display_name", null, null, displayCode));
    	if(prodJson.getFamily().equals("options_swatches")) {
	    	prodJson.addAttributeValues(new AttributeJson("grade", null, null, labelInfo[0].trim().toLowerCase()));
	    	prodJson.addAttributeValues(new AttributeJson("manufacturer_name", null, null, labelInfo[1].trim().toLowerCase()));
	    	prodJson.addAttributeValues(new AttributeJson("marketing_color", null, null, labelInfo[2].trim()));
    	} else {
	    	prodJson.addAttributeValues(new AttributeJson("option_value", null, null, labelInfo[2].trim()));
    	}

    	return prodJson;
    }
    
    private ProductJson createProductPrice( Map<String, Object> data, String baseSku, String identifier, String[] labelInfo, String price) {
    	
    	ProductJson prodJson = new ProductJson();
    	prodJson.setIdentifier(identifier);
    	prodJson.setFamily("product_pricing");
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "P"));
    	if (price.indexOf("$") >0) {
    		if (price.indexOf("-") > -1) {
    			price = "-" + price.substring(price.indexOf("$")+1);
    		} else {
    			price = price.substring(price.indexOf("$")+1);
    		}
    	}
    	prodJson.addAttributeValues(new AttributeJson("retail_price", null, null, price));
    	prodJson.addAttributeValues(new AttributeJson("base_sku", null, null, baseSku));
    	if (StringUtils.isNotEmpty(labelInfo[0])) {
    		prodJson.addAttributeValues(new AttributeJson("grade", null, null, labelInfo[0]));
    	} else {
        	prodJson.addAttributeValues(new AttributeJson("option_sku", null, null, labelInfo[3]));
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
	    	writerData.getImages().add(new MediaJson(sku, url, attributeCode, null, null));
    	}
    }

    private void processDocuments(String sku, WriterData writerData) {
    	if (StringUtils.isNotEmpty(descripTransformer.getProductResourceCare())){
        	writerData.getDocuments().add(new MediaJson(sku, descripTransformer.getProductResourceCare(), "product_resources_care", null, null));
    	} 
    	if (StringUtils.isNotEmpty(descripTransformer.getProductResourceCatalog())){
        	writerData.getDocuments().add(new MediaJson(sku, descripTransformer.getProductResourceCatalog(), "product_resources_catalog", null, null));
    	}
    	if (StringUtils.isNotEmpty(descripTransformer.getProductResourceSpecification())){
        	writerData.getDocuments().add(new MediaJson(sku, descripTransformer.getProductResourceSpecification(), "product_resources_specifications", null, null));
    	}
    	int counter = 2;
    	if (descripTransformer.getProductResources() != null) { 
	    	for (String resource: descripTransformer.getProductResources()) {
	    		if (counter <= Constants.RESOURCE_MAX_SUFFIX) {
	        	writerData.getDocuments().add(new MediaJson(sku, resource, "product_resources_pdf_"+counter, null, null));
	    		}
	        	counter++;
	    	}
    	}
    }
    
	public AttributeJson getAttributeJson(PIMValue pimvalue, String data) {
		if (pimvalue.isTextArea() || pimvalue.isText()) {
			addMultiValueTextArea(pimvalue.getCode(), data);
			return null;
		} else if (pimvalue.isNumber()) {
			return new AttributeJson(pimvalue.getCode(), null, null, data);
		} else if (pimvalue.isMetric()) {
			return new AttributeJson(pimvalue.getCode(), null, null, data, pimvalue.getMetric(), null);
		} else if (pimvalue.isBoolean()) {
			if (data.equalsIgnoreCase("yes")) {
				return new AttributeJson(pimvalue.getCode(), null, null, "true", false);	
			} else {
				return new AttributeJson(pimvalue.getCode(), null, null, "false", false);	
			}
		} else if (pimvalue.isMultiSelect()) {
			String newcode = pimvalue.getCode().trim()+"-"+data.trim();
			addMultiSelectData(pimvalue.getCode(), getAttributeCode(pimvalue.getCode(), newcode));
			return null;
		} else if (pimvalue.isSimpleSelect()) {
			String code = getAttributeCode(pimvalue.getCode(), pimvalue.getCode().trim()+"-"+data.trim());
			if(code != null) {
				return new AttributeJson(pimvalue.getCode(), null, null, code);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String getAttributeCode(String attribute, String label) {
		if (optionAttributes.get(label.toLowerCase()) == null) {
			LOGGER.error(String.format("SKU: %s , Attribute: %s,  Option label %s not found in PIM ", writerData.getSku(), attribute, label));
		}
		return optionAttributes.get(label.toLowerCase());
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
    	if (StringUtils.isNotEmpty(label)) {
	    	if (label.indexOf("(+$") > 0) {
	    		priceString = label.substring(label.indexOf("(+$")+1);
	    		priceString = priceString.substring(0, priceString.indexOf(")"));
	    	} else if (label.indexOf("(-$") >0) {
	    		priceString = label.substring(label.indexOf("(-$")+1);
	    		priceString = priceString.substring(0, priceString.indexOf(")"));
	    	}
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
	
	public String getFamilyCodeOptions(String family) {
    	if(StringUtils.equalsIgnoreCase(family, "dropdown")) {
    		family = "options_dropdown";
    	}else {
    		family = "options_swatches";
    	}		
		return family;
	}
	
	protected void addMultiValueTextArea(String key, String Value) {
		if (Value != null) { 
			if (multiValueTextArea.get(key) == null) {
				multiValueTextArea.put(key, new HashSet<>());
			}
			multiValueTextArea.get(key).add(Value);
		}
	}
	
	private String[] parseLabel(String label, String id) {
		String[] labelArr = new String[]{"", "", "", ""};
		if (label.indexOf("Grade") > -1) {
			String[] gradeLabel = label.split(" ");
			if (gradeLabel.length >= 3) { 
				labelArr[0] = gradeLabel[1];
				labelArr[1] = gradeLabel[2];
				for (int i=3; i<gradeLabel.length; i++ ) {
					if (gradeLabel[i].indexOf("(") > -1 || gradeLabel[i].indexOf("--") > -1) {
						break;
					}
					labelArr[2] = labelArr[2] + gradeLabel[i] + " ";
				}
				labelArr[3] = label.substring(label.indexOf("--") + 2);
			}
		} else if (label.indexOf("--") >-1){
			if (label.indexOf("(") > -1) { 
				labelArr[2] = label.substring(0,label.indexOf("("));
			} else {
				labelArr[2] = label.substring(0,label.indexOf("--"));
			}
			labelArr[3] = label.substring(label.indexOf("--") + 2) + "_" + id;
		} else {
			if (label.indexOf("(") > -1) { 
				labelArr[2] = label.substring(0,label.indexOf("("));
			} else {
				labelArr[2] = label;
			}
			labelArr[3] = id;
		}
		
		return labelArr;
	}
}


