package com.striketru.bc2akeneo.model;

public class AttributeJson {

	private String locale;
	private String scope;
	private String data;
	
	public AttributeJson(String locale, String scope, String data) {
		
		this.locale = locale;
	
		this.data = data;
		this.scope = scope;
		
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	
	
	
	}
