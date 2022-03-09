package com.striketru.bc2akeneo.model;

public class ProductJson {
	private String products;
	private String product_models;
	private String groups;
	
	public ProductJson(String products, String product_models, String groups) {
		
		this.products = products;
		this.product_models = product_models;
		this.groups = groups;
		
	}

	public String getproducts() {
		return products;
	}

	public void setproducts(String products) {
		this.products = products;
	}

	public String getproduct_models() {
		return product_models;
	}

	public void setproduct_models(String product_models) {
		this.product_models = product_models;
	}

	public String getgroups() {
		return groups;
	}

	public void setgroups(String groups) {
		this.groups = groups;
	}

}
