package com.striketru.bc2akeneo.model;

public class PIMValue {
	
	public PIMValue(String code, String type) {
		this.code = code;
		this.dataType = type;
	}
	
	private String code;
	private String dataType;

	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public boolean isText() {
		return getDataType().endsWith("text");
	}
	public boolean isTextArea() {
		return getDataType().endsWith("textarea");
	}
	public boolean isNumber() {
		return getDataType().endsWith("number");
	}
	public boolean isMetric() {
		return getDataType().endsWith("metric");
	}
	public boolean isBoolean() {
		return getDataType().endsWith("boolean");
	}
	public boolean isMultiSelect() {
		return getDataType().endsWith("multiselect");
	}
	public boolean isSimpleSelect() {
		return getDataType().endsWith("simpleselect");
	}
	public boolean isImage() {
		return getDataType().endsWith("image");
	}
	public boolean isFile() {
		return getDataType().endsWith("file");
	}

	
}
