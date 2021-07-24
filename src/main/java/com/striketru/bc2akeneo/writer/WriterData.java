package com.striketru.bc2akeneo.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.striketru.conn.base.model.BaseData;
import com.striketru.pim.model.ImageJson;
import com.striketru.pim.model.ProductJson;

public class WriterData extends  BaseData {

	private ProductJson baseProduct;
	private Map<String, ProductJson> optionsProduct = new HashMap<>();
	private ProductJson productAssociation;
	private List<ProductJson> price = new ArrayList<>();
	private List<ImageJson> images = new ArrayList<>();
	private Report report;
	
	public WriterData(String sku) {
		this.sku = sku;
		this.report = new Report(sku);
	}
		
	public ProductJson getBaseProduct() {
		return baseProduct;
	}
	public void setBaseProduct(ProductJson baseProduct) {
		this.baseProduct = baseProduct;
	}
	public Map<String, ProductJson> getOptionsProduct() {
		return optionsProduct;
	}
	public void setOptionsProduct(Map<String, ProductJson> optionsProduct) {
		this.optionsProduct = optionsProduct;
	}
	public ProductJson getProductAssociation() {
		return productAssociation;
	}
	public void setProductAssociation(ProductJson productAssociation) {
		this.productAssociation = productAssociation;
	}
	public List<ProductJson> getPrice() {
		return price;
	}
	public void setPrice(List<ProductJson> price) {
		this.price = price;
	}
	public void addPrice(ProductJson product) {
		price.add(product);
	}
	public List<ImageJson> getImages() {
		return images;
	}
	public void setImages(List<ImageJson> images) {
		this.images = images;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	
}
