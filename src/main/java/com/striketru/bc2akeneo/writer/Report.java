package com.striketru.bc2akeneo.writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.constants.BuilderConstants;

public class Report {
	private static final Logger LOGGER = LogManager.getLogger("ResultLog");
    private static final Logger CSVLOGGER = LogManager.getLogger("FileCountAppenderCSV");
    private static final String SKU_STR = "SKU->%s:  ";
    private String sku;
	private int readModifierCount;
	private int readVariantsCount;
	private int readOptionsCount;
	private int writeProductOptionsCount;
	private List<String> responses;
	
	public Report(String sku) {
		this.sku = sku;
	}
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getReadModifierCount() {
		return readModifierCount;
	}
	public void setReadModifierCount(int readModifierCount) {
		this.readModifierCount = readModifierCount;
	}
	public int getReadVariantsCount() {
		return readVariantsCount;
	}
	public void setReadVariantsCount(int readVariantsCount) {
		this.readVariantsCount = readVariantsCount;
	}
	public int getReadOptionsCount() {
		return readOptionsCount;
	}
	public void setReadOptionsCount(int readOptionsCount) {
		this.readOptionsCount = readOptionsCount;
	}
	public int getWriteProductOptionsCount() {
		return writeProductOptionsCount;
	}
	public void setWriteProductOptionsCount(int writeProductOptionsCount) {
		this.writeProductOptionsCount = writeProductOptionsCount;
	}
	public List<String> getResponses() {
		return responses;
	}
	public void setResponses(List<String> responses) {
		this.responses = responses;
	}
	public void addResponse(String response) {
		if (responses == null) {
			responses = new ArrayList();
		}
		responses.add(response);
	}
	public void writeLog() {
		CSVLOGGER.info("bc2Akeneo", sku, getReadModifierCount(), getReadVariantsCount(), 
				getReadOptionsCount(), getWriteProductOptionsCount());
	}
	public void writeResponse() {
//		String logString = String.format(SKU_STR, sku);
//		for (String response: getResponses()) {
//			LOGGER.info(logString + response);
//		}
	}
	
	public void addCount(String type, int count) {
		if (StringUtils.equalsIgnoreCase(type, BuilderConstants.MODIFIERS)) {
			setReadModifierCount(count);
		} else if (StringUtils.equalsIgnoreCase(type, BuilderConstants.OPTIONS)) {
			setReadOptionsCount(count);
		} else if (StringUtils.equalsIgnoreCase(type, BuilderConstants.VARIANTS)) {
			setReadVariantsCount(count);
		}
	}
	
}
