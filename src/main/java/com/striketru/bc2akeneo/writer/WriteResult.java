package com.striketru.bc2akeneo.writer;

import com.striketru.conn.base.model.Result;

public class WriteResult extends Result {

	static final String COUNT_STR = "SKU: %s, OptionsCount = %d, priceCount = %d, imageCount = %d";
	private int optionsCount;
	private String optionsResponse;
	private int priceCount;
	private String priceResponse;
	private int imageCount;
	private String imageResponse;
	private boolean isVariants;
	private boolean isModifiers;
	private boolean hasOptions;
	
	public WriteResult(String sku) {
		setSku(sku);
	}

	public int getOptionsCount() {
		return optionsCount;
	}

	public void setOptionsCount(int optionsCount) {
		this.optionsCount = optionsCount;
	}
	public void incrementOptionsCount() {
		this.optionsCount = this.optionsCount + 1;
	}
	

	public String getOptionsResponse() {
		return optionsResponse;
	}

	public void setOptionsResponse(String optionsResponse) {
		this.optionsResponse = optionsResponse;
	}
	
	public void appendOptionsResponse(String optionsResp) {
		this.optionsResponse = this.optionsResponse + optionsResp;
	}

	public int getPriceCount() {
		return priceCount;
	}

	public void setPriceCount(int priceCount) {
		this.priceCount = priceCount;
	}
	
	public void incrementPriceCount(int priceCount) {
		this.priceCount = this.priceCount + priceCount;
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
	
	public String countString() {
		return String.format(COUNT_STR, getSku(), getOptionsCount(), getPriceCount(), getImageCount());
	}
	
	public boolean isVariants() {
		return isVariants;
	}

	public void setVariants(boolean isVariants) {
		this.isVariants = isVariants;
	}

	public boolean isModifiers() {
		return isModifiers;
	}

	public void setModifiers(boolean isModifiers) {
		this.isModifiers = isModifiers;
	}

	public boolean isHasOptions() {
		return hasOptions;
	}

	public void setHasOptions(boolean hasOptions) {
		this.hasOptions = hasOptions;
	}

	@Override
	public String toString() {
		return "WriteResult [sku=" + getSku() + ", optionsCount=" + optionsCount + ", optionsResponse=" + optionsResponse
				+ ", priceCount=" + priceCount + ", priceResponse=" + priceResponse + ", imageCount=" + imageCount
				+ ", imageResponse=" + imageResponse + ", variants=" + isVariants + ", isModifiers=" + isModifiers + " hasOptions=" + hasOptions +"]";
	}
	

	
}
