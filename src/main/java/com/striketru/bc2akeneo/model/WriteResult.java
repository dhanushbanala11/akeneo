package com.striketru.bc2akeneo.model;

public class WriteResult {

	static final String COUNT_STR = "SKU: %s, OptionsCount = %d, priceCount = %d, imageCount = %d";
	private String sku;
	private int optionsCount;
	private String optionsResponse;
	private int priceCount;
	private String priceResponse;
	private int imageCount;
	private String imageResponse;
	private boolean isVariants;
	private boolean isModifiers;
	private boolean hasOptions;
	private String baseProductResp;
	private int variantsCount;
	private int optionCount;
	private int modifiersCount;

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
	
	public String getBaseProductResp() {
		return baseProductResp;
	}

	public void setBaseProductResp(String baseProductResp) {
		this.baseProductResp = baseProductResp;
	}
	
	public int getVariantsCount() {
		return variantsCount;
	}

	public void setVariantsCount(int variantsCount) {
		this.variantsCount = variantsCount;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public int getModifiersCount() {
		return modifiersCount;
	}

	public void setModifiersCount(int modifiersCount) {
		this.modifiersCount = modifiersCount;
	}

	@Override
	public String toString() {
		return "WriteResult [sku=" + sku + ", optionsCount=" + optionsCount + ", optionsResponse=" + optionsResponse
				+ ", priceCount=" + priceCount + ", priceResponse=" + priceResponse + ", imageCount=" + imageCount
				+ ", imageResponse=" + imageResponse + ", variants=" + isVariants + ", isModifiers=" + isModifiers + " hasOptions=" + hasOptions + ", BaseResponse=" + baseProductResp +"]";
	}
	

	
}
