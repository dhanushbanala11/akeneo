package com.striketru.pim.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.striketru.pim.util.PIMRequestUtil;

public class ProductJson implements PIMRequestUtil{

	private String identifier;
	private String family;
	private List<Object> categories;
	private String parent;
	private List<AttributeJson> values;
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public List<String> getCategoriesAsString() {
		List<String> catgoriesStr = new ArrayList<>(categories.size());
		for (Object obj: categories) {
			catgoriesStr.add(String.valueOf(obj));
		}
		
		return catgoriesStr;
	}
	public List<Object> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Object> categories) {
		this.categories = categories;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public List<AttributeJson> getValues() {
		return values;
	}
	public void setValues(List<AttributeJson> values) {
		this.values = values;
	}
	public void addAttributeValues(AttributeJson attributeJson) {
		if (values == null) {
			values = new ArrayList<>();
		}
		values.add(attributeJson);
	}

	public String createRequest() {
    	StringBuilder strbuild = new StringBuilder("{");
    	strbuild.append(createKeyValueJson("identifier", getIdentifier()));
    	strbuild.append(",").append(createKeyValueJson("family", getFamily()));
    	if (getCategories() != null && getCategories().size() > 0) { 
    		strbuild.append(",").append(createKeyValueListJson("categories", getCategoriesAsString()));
    	}
    	if (getValues() != null && getValues().size() > 0) {
    		strbuild.append(",").append("\"values\": ").append(createRequestForValues());
    	}
    	strbuild.append("}");
    	return strbuild.toString();	
	}
	
	private String createRequestForValues() {
		StringBuilder strbuild = new StringBuilder("{");
    	if (getValues() != null) { 
    		boolean isFirstIteration = true;
    		for (AttributeJson attributeJson : getValues()) {
    			if (StringUtils.isNotEmpty(attributeJson.getData()) || (attributeJson.getDataList() != null && attributeJson.getDataList().size() >0) ) {
	    			if (isFirstIteration) {
	    				isFirstIteration = false;
	    				strbuild.append(attributeJson.createJson());
	    			} else {
	    				strbuild.append(",").append(attributeJson.createJson());
	    			}
    			}
    		}
    	}
		strbuild.append("}");
    	return strbuild.length() <= 2? null :  strbuild.toString();
	}
    		

	
}
