package com.striketru.pim.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.striketru.pim.util.PIMRequestUtil;

public class AttributeJson implements PIMRequestUtil {

	private String attributeId;
	private String locale;
	private String scope;
	private boolean isDataList;
	private List<String> dataList;
	private String data;
	private boolean isDataInQuotes = true;
	private String unit;
	private String link;

	public AttributeJson(String attributeId, String locale, String scope, String data, String unit, String link) {
		this.attributeId = attributeId;
		this.locale = locale;
		this.scope = scope;
		this.data = data;
		this.unit = unit;
		this.link = link;
	}

	public AttributeJson(String attributeId, String locale, String scope, String data) {
		this.attributeId = attributeId;
		this.locale = locale;
		this.scope = scope;
		this.data = data;
		this.isDataList = false;
	}

	public AttributeJson(String attributeId, String locale, String scope, String data, boolean isDataInQuotes) {
		this.attributeId = attributeId;
		this.locale = locale;
		this.scope = scope;
		this.data = data;
		this.isDataInQuotes = isDataInQuotes;
		this.isDataList = false;
	}

	public AttributeJson(String attributeId, String locale, String scope, List<String> data) {
		this.attributeId = attributeId;
		this.locale = locale;
		this.scope = scope;
		this.dataList = data;
		this.isDataList = true;
	}
	
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean isIsDataInQuotes() {
		return isDataInQuotes;
	}

	public void setIsDataInQuotes(boolean isdataInQuotes) {
		this.isDataInQuotes = isdataInQuotes;
	}

	public boolean isDataList() {
		return isDataList;
	}

	public void setDataList(boolean isDataList) {
		this.isDataList = isDataList;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public boolean isUnitExists() {
		return StringUtils.isNotEmpty(unit);
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public boolean isLinkExists() {
		return StringUtils.isNotEmpty(link);
	}
	
    public String createJson() {
		if (isUnitExists()) {
			return createAttributeUnitJson(getAttributeId(), getLocale(), getScope(), getUnit(), getData());
		} else if (isLinkExists()) {
			return createAttributeImageJson(getAttributeId(), getLocale(), getScope(), getLink(), getData());
		} else if (isDataList()) {
			return createAttributeArrayJson(getAttributeId(), getLocale(), getScope(), getDataList());
		} else {
			return createAttributeJson(getAttributeId(), getLocale(), getScope(), getData(), isDataInQuotes);
		}
    }
	
}
