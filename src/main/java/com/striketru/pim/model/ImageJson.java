package com.striketru.pim.model;

import com.striketru.pim.util.PIMRequestUtil;

public class ImageJson implements PIMRequestUtil {
	private String identifier;
	private String url;
	private String attribute_code;
	private String locale;
	private String scope;
	
	public ImageJson(String identifier, String url, String attribute_code, String locale, String scope) {
		this.identifier = identifier;
		this.url = url;
		this.attribute_code = attribute_code;
		this.locale = locale;
		this.scope = scope;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAttribute_code() {
		return attribute_code;
	}
	public void setAttribute_code(String attribute_code) {
		this.attribute_code = attribute_code;
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
	
}
