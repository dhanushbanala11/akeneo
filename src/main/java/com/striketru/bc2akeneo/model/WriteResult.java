package com.striketru.bc2akeneo.model;

public class WriteResult {

	private String sku;
	private int optionsCount;
	private String optionsResponse;
	private int priceCount;
	private String priceResponse;
	private int imageCount;
	private String imageResponse;
	
	public WriteResult(String sku) {
		this.sku = sku;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getOptionsCount() {
		return optionsCount;
	}

	public void setOptionsCount(int optionsCount) {
		this.optionsCount = optionsCount;
	}

	public String getOptionsResponse() {
		return optionsResponse;
	}

	public void setOptionsResponse(String optionsResponse) {
		this.optionsResponse = optionsResponse;
	}

	public int getPriceCount() {
		return priceCount;
	}

	public void setPriceCount(int priceCount) {
		this.priceCount = priceCount;
	}

	public String getPriceResponse() {
		return priceResponse;
	}

	public void setPriceResponse(String priceResponse) {
		this.priceResponse = priceResponse;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public String getImageResponse() {
		return imageResponse;
	}

	public void setImageResponse(String imageResponse) {
		this.imageResponse = imageResponse;
	}

	@Override
	public String toString() {
		return "WriteResult [sku=" + sku + ", optionsCount=" + optionsCount + ", optionsResponse=" + optionsResponse
				+ ", priceCount=" + priceCount + ", priceResponse=" + priceResponse + ", imageCount=" + imageCount
				+ ", imageResponse=" + imageResponse + "]";
	}
	

	
}
