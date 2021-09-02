package com.striketru.bc2akeneo.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
	private Map<String, Object> optionsDisplayNameBySku = new LinkedHashMap<String, Object>();
	
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
    	prodJson.addAttributeValues(new AttributeJson("brand", null, null, getStringDataFromMap(data, "brand_id")));

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
					prodJson.addAttributeValues(new AttributeJson("product_video_url_" + count, null, null, videoUrl));
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
						// Example SKUs
						// Grade B Ashbee (+$129.00) -- 379;
						// Grade A Atlantic Blue Stripe -- 649; 
						// Natural Gas -- L54TRF-NG
						// Wood Burning with Natural Gas Kit (+$406.00)
						// 18 Inch x 6 Inch - Oil Rubbed Finish - Natural 
						// Gas (+$100.00) -- OB-AFPPSIT-N-18
						/* label[0] - Grade, label[1] - Manufacturer Name, label[2] - Marketing Color, label[3] - Color Code */
						String[] labelInfo = parseLabel(label.trim().replaceAll("  +", " "), getStringDataFromMap(optionVal, "id"));
						String optionSku = getSku(labelInfo, optionVal, "option", null);
						
						String price = getPriceFromLabel(label);
						if (price != null ) {
							ProductJson priceRecord = createPriceRecord(optionVal, displayName, writerData, labelInfo, price, priceSkus);
							if(priceRecord!=null)
								writerData.addPrice(priceRecord);
						}
						if (StringUtils.equalsIgnoreCase(type, Constants.VARIANTS)) {
							displayName = optionVal.get(Constants.OPTION_DISPLAY_NAME).toString();
						}
						if (StringUtils.equalsIgnoreCase(type, Constants.MODIFIERS) || StringUtils.equalsIgnoreCase(type, Constants.OPTIONS)) {
							if (optionVal.get("value_data") != null) {
								processImages(optionSku, getStringDataFromMap((Map<String, Object>)optionVal.get("value_data"), "image_url"), "-1", writerData);
							}
						}
						if(!label.toLowerCase().contains("grade")) { // For Non graded options, no consolidation happen
							ProductJson optionProduct = createOptionProduct(optionVal, labelInfo, writerData.getSku(), displayName, optionType, optionsDisplayNameBySku);
							writerData.getOptionsProduct().put(optionProduct.getIdentifier(), optionProduct);
						}else {
							writerData.getOptionsProduct().put(label, createOptionProduct(optionVal, labelInfo, writerData.getSku(), displayName, optionType, optionsDisplayNameBySku));
//							ProductJson optionProduct = createOptionProduct(optionVal, labelInfo, writerData.getSku(), displayName, optionType, optionsDisplayNameBySku);
//							writerData.getOptionsProduct().put(optionProduct.getIdentifier(), optionProduct);
						}
						count++;
					}
				}
			}
			
			List<ProductJson> uniquePrices = new ArrayList<ProductJson>();
			
			Set<String> priceSkus = new HashSet<String>();
			for(ProductJson priceRecord : writerData.getPrice()) {
				if(!priceSkus.contains(priceRecord.getIdentifier())) {
					uniquePrices.add(priceRecord);
					priceSkus.add(priceRecord.getIdentifier());
				}
			}
			writerData.setPrice(uniquePrices);
			writerData.getReport().addCount(type, count);
		}
    }

    private ProductJson createOptionProduct(Map<String, Object> data, String[] labelInfo, String baseSku, String displayName, String optionType, Map<String, Object> optionsDisplayNameBySku) {
    	
    	ProductJson prodJson = new ProductJson();
    	ArrayList<String> displayCode = new ArrayList<String>();
    	String dname = getAttributeCode("display_name_option", "display_name_option"+"-"+displayName);
    	displayCode.add(dname);
    	String family  = "options_swatches";
    	if(StringUtils.equalsIgnoreCase(optionType, "dropdown")) {
    		family = "options_dropdown";
    	}
    	
    	String idetntifier = getSku(labelInfo, data, "option", null);
    			
    			
    	ArrayList<String> values = (ArrayList<String>) optionsDisplayNameBySku.get(idetntifier);
    	if(values!=null && !values.contains(displayCode) && data.get("label").toString().toLowerCase().contains("grade")) {
    		String newDisplayValue = getAttributeCode("display_name_option", "display_name_option"+"-"+displayName);
    		if(!values.contains(newDisplayValue)) {
    			values.add(newDisplayValue);
    		}
    		optionsDisplayNameBySku.put(idetntifier, values);
    		for (String value : values) {
				if(!displayCode.contains(value)) {
					displayCode.add(value);
				}
			}
    	}else {
    		optionsDisplayNameBySku.put(idetntifier, displayCode);
    	}
    	prodJson.setFamily(family);
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "O"));
    	prodJson.addAttributeValues(new AttributeJson("display_name_option", null, null, displayCode));
    	prodJson.setIdentifier(idetntifier);
    	if(prodJson.getFamily().equals("options_swatches")) {
	    	prodJson.addAttributeValues(new AttributeJson("manufacturer_name", null, null, labelInfo[1].trim().toLowerCase()));
	    	prodJson.addAttributeValues(new AttributeJson("marketing_color", null, null, labelInfo[2].trim()));
	    	//prodJson.setIdentifier(baseSku+"_"+getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim())+"--"+displayName);	    	
	    	if(data.get("label").toString().contains("--")) {
	    		if(StringUtils.isNotBlank(labelInfo[0])) {
	    			prodJson.addAttributeValues(new AttributeJson("color_code", null, null, labelInfo[3])); // labelInfo[3]
	    			prodJson.addAttributeValues(new AttributeJson("grade", null, null, getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim())));
	    		}
    		}
    	} else {
	    	prodJson.addAttributeValues(new AttributeJson("option_value", null, null, labelInfo[2].trim()));
    	}
    	return prodJson;
    }
    
    private String getSku(String[] labelInfo, Map<String, Object> data, String skuType, String baseSku) {
    	if(skuType.equals("option")) {
	    	if(data.get("label").toString().contains("--") && StringUtils.isNotBlank(labelInfo[0]))
	    		// -- represents Color Code and labelInfo[0] represents grade
	    		return labelInfo[3].trim()+"_"+labelInfo[1].trim()+"_"+getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim());
			else {
				if(!StringUtils.isBlank(labelInfo[3]))
					// -- represents Color Code
					return labelInfo[3].trim()+"_"+data.get("id");
				else 
					return data.get("id").toString();
			}
			//else
				//return labelInfo[3].trim()+"_"+data.get("id");
    	}else { // indicates Price
    		if (StringUtils.isNotEmpty(labelInfo[0]))
        		return baseSku+"_"+getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim());
        	else {
        		//return baseSku+"_"+labelInfo[3];
        		if(!StringUtils.isBlank(labelInfo[3]))
					// -- represents Color Code
					return baseSku+"_"+labelInfo[3];
				else 
					return baseSku+"_"+data.get("id");
        	}
        		
    	}
    }
    
    /**
     * @deprecated
     * @param data
     * @param displayName
     * @param writeData
     * @param labelInfo
     * @param price
     * @param priceSkus
     * @return
     */
    private ProductJson createProductPrice(Map<String, Object> data, String displayName, WriterData writeData, String[] labelInfo, String price, Set<String> priceSkus) {
    	String baseSku = writeData.getSku();
    	/* SKU generation starts */
    	String priceSku = getSku(labelInfo, data, "price", baseSku);
    	/* SKU generation ends */
    	
    	ProductJson prodJson = new ProductJson();
    	prodJson.addAttributeValues(new AttributeJson("sku_type", null, null, "P"));
    	if (price.indexOf("$") >0) {
    		if (price.indexOf("-") > -1) {
    			price = "-" + price.substring(price.indexOf("$")+1).trim();
    		} else {
    			price = price.substring(price.indexOf("$")+1).trim();
    		} 
    	}
    	
    	String displayCode = getAttributeCode("display_name_price", "display_name_price"+"-"+displayName);
    	
    	if(priceSkus.contains(priceSku)) {
    		List<ProductJson> prices = writeData.getPrice();
    		
    		String newIdentifier = null;
    		for(ProductJson priceProd :prices) {
    			if(priceProd.getIdentifier().equals(priceSku) && priceProd.getValues().get(1).getData().equals(displayCode)) {
    				return null;
    			}else if(priceProd.getIdentifier().equals(priceSku) && !priceProd.getValues().get(1).getData().equals(displayCode)) {
    				newIdentifier = priceProd.getIdentifier()+"--"+displayCode;
    				prodJson.setIdentifier(newIdentifier);
    				int index = writeData.getPrice().indexOf(priceProd);
    				priceProd.setIdentifier(priceProd.getIdentifier()+"--"+priceProd.getValues().get(1).getData());
    				priceSkus.add(newIdentifier);
    				break;
    			}else if(!priceSkus.contains(priceSku)){
    				prodJson.setIdentifier(priceSku);
    	    		priceSkus.add(priceSku);
    			}
    		}
    	}else {
    		prodJson.setIdentifier(priceSku);
    		priceSkus.add(priceSku);
    	}
    	
    	prodJson.addAttributeValues(new AttributeJson("display_name_price", null, null, displayCode));
    	prodJson.addAttributeValues(new AttributeJson("price", null, null, price));
    	prodJson.addAttributeValues(new AttributeJson("base_sku", null, null, baseSku));
    	if (StringUtils.isNotEmpty(labelInfo[0])) {
    		prodJson.setFamily("product_pricing_graded");
    		prodJson.addAttributeValues(new AttributeJson("grade", null, null, getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim())));
    	} else {
    		prodJson.setFamily("product_pricing");
        	prodJson.addAttributeValues(new AttributeJson("option_sku", null, null, labelInfo[3]));
    	}
    	return prodJson;
    }
    
    /**
     * Replacement method for createProductPrice
     * This method is used to create the price record from the BC options
     * @param data
     * @param displayName
     * @param writeData
     * @param labelInfo
     * @param price
     * @param priceSkus
     * @return ProductJson
     */
    private ProductJson createPriceRecord(Map<String, Object> data, String displayName, WriterData writeData, String[] labelInfo, String price, Set<String> priceSkus) {
    	ProductJson productJson = new ProductJson();
    	String baseSku = writeData.getSku();
    	/* SKU generation starts */
    	String currentPriceSku = getSku(labelInfo, data, "price", baseSku);
    	/* SKU generation ends */
    	
    	price = getPrice(price);
    	
    	productJson.addAttributeValues(new AttributeJson("sku_type", null, null, "P"));
    	
    	String currentDisplayCode = getAttributeCode("display_name_price", "display_name_price"+"-"+displayName);
    	
    	if(!labelInfo[0].isEmpty()) { // Graded Price
    		productJson.setFamily("product_pricing_graded");
    		productJson.addAttributeValues(new AttributeJson("base_sku", null, null, baseSku));
    		productJson.addAttributeValues(new AttributeJson("grade", null, null, getAttributeCode("grade", "grade"+"-"+labelInfo[0].toLowerCase().trim())));
    		productJson.addAttributeValues(new AttributeJson("display_name_price", null, null, currentDisplayCode));
    		productJson.addAttributeValues(new AttributeJson("price", null, null, price));
	    	if(!priceSkus.contains(currentPriceSku)) { // Price SKU processed for the 1st time
	    		priceSkus.add(currentPriceSku);
	    		productJson.setIdentifier(currentPriceSku);
	    	}else { // Price SKU was processed at-least once before
	    		List<ProductJson> writerPrices = writerData.getPrice();
	    		for(ProductJson writerPrice :writerPrices) {
	    			String existingPriceSku = writerPrice.getIdentifier();
	    			String existingDisplayName = writerPrice.getValues().get(3).getData();
	    			
	    			if(existingPriceSku==null) {continue;}
	    			if(!existingPriceSku.equals(currentPriceSku)) { // If existing SKU doesnt match current SKU, skip it and continue
	    				continue;
	    			}
	    			
	    			if(existingPriceSku.equals(currentPriceSku) && !existingPriceSku.contains("--")) { // Current SKU exists with Writer data
	    				if(!existingDisplayName.equals(currentDisplayCode)) { //if the existing display name is not the same as current display name
	    					if(!existingPriceSku.contains("--")) {
		    					/* Update existing SKU in writer data */
		    					writerPrice.setIdentifier(existingPriceSku+"--"+existingDisplayName);
	    					}
	    					/* Same Price SKU with a different Display Name, adjusting SKU to include display name */
	    					productJson.setIdentifier(currentPriceSku+"--"+currentDisplayCode);
	    					break;
	    				}else { //if the existing display name is the same as current display name
	    					return null;
	    				}
	    			}else if(existingPriceSku.equals(currentPriceSku) && existingPriceSku.contains("--")) {
	    				continue;
	    			}
//	    			else if(!existingPriceSku.equals(currentPriceSku) && currentDisplayCode.equals(existingDisplayName)) {
//	    				continue;
//	    			}
	    			else 
	    				return null;
	    		}
	    	}
    	}else { // Non graded Price
    		productJson.setIdentifier(currentPriceSku);
    		productJson.setFamily("product_pricing");
    		productJson.addAttributeValues(new AttributeJson("base_sku", null, null, baseSku));
    		productJson.addAttributeValues(new AttributeJson("option_sku", null, null, labelInfo[3]));
    		productJson.addAttributeValues(new AttributeJson("display_name_price", null, null, currentDisplayCode));
    		productJson.addAttributeValues(new AttributeJson("price", null, null, price));
        	priceSkus.add(currentPriceSku);
    	}
    	return productJson;
    }
    
    private String getPrice(String price) {
    	if (price.indexOf("$") >0) {
    		if (price.indexOf("-") > -1) {
    			price = "-" + price.substring(price.indexOf("$")+1).trim();
    		} else {
    			price = price.substring(price.indexOf("$")+1).trim();
    		} 
    	}
    	return price;
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
			String code = getAttributeCode(pimvalue.getCode(), pimvalue.getCode().trim()+"-"+data.toLowerCase().trim());
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
			LOGGER.debug(String.format("SKU: %s , Attribute: %s,  Option label %s not found in PIM ", writerData.getSku(), attribute, label));
		}
		return optionAttributes.get(label.toLowerCase());
	}

	public boolean isVariantNotBaseSku(Map<String, Object> data, String baseSKU) {
		String varSKU = getStringDataFromMap(data, "sku");
		return !varSKU.equalsIgnoreCase(baseSKU);
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
	    	}else if(label.indexOf("($") > 0) {
	    		priceString = label.substring(label.indexOf("($")+2);
	    		priceString = priceString.substring(0, priceString.indexOf(")"));
	    	}
    	}
    	return priceString;
    }
    
	public String getFamilyCode(Map<String, Object> data) {
		List<Integer> categories = (List<Integer>) data.get("categories");	
		if (categories!= null && categories.size() > 0) {
			for (Integer code: categories) {
				if ( families.get(String.valueOf(code).trim()) != null) {
					return families.get(String.valueOf(code).trim());
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
			labelArr[3] = label.substring(label.indexOf("--") + 2); // + "_" + id
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