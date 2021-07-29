package com.striketru.bc2akeneo.transformer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DescriptionTransformer {
	private static final Logger LOGGER = LogManager.getLogger(BC2PIMTransformer.class);
	
	private String productDescription;
	private String careInfo;
	private String specsInfo;
	private String shipInfo;
	private String pdfInfo;
	private String productResourceCatalog;
	private String productResourceCare;
	private String productResourceSpecification;
	private List<String> productResources;
	
	public DescriptionTransformer(String description) {
		parsefield(description);
	}
	
	private void parsefield(String description) {
		getTabInfo(description);
//		LOGGER.info(productDescription);
//		LOGGER.info(pdfInfo);
//		LOGGER.info(specsInfo);
//		LOGGER.info(getSpecsInfo());
//		LOGGER.info(careInfo);
//		LOGGER.info(getCareInfo());
//		LOGGER.info(shipInfo);
//		LOGGER.info(getShipInfo());
	}

	private void getTabInfo(String description) {
		String tempArray[] = description.split("<div");
		
		if (tempArray.length > 0 ) {
			productDescription = tempArray[0];
			for (String line: tempArray) {
				if (line.indexOf("id=\\\"pdfTab\\\"") > -1 ) {
					pdfInfo = line;
				} else if (line.indexOf("id=\\\"specsTab\\\"") > -1) { 
					specsInfo = line;
				} else if (line.indexOf("id=\\\"shipTab\\\"") > -1 ) { 
					shipInfo = line;
				} else if (line.indexOf("id=\\\"careTab\\\"") > -1 ) { 
					careInfo = line;
				}
				
			}
		}
	}
	
	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getCareInfo() {
		return careInfo == null? "": careInfo.substring(careInfo.indexOf(">") +1, careInfo.indexOf("</div>"));
	}

	public void setCareInfo(String careInfo) {
		this.careInfo = careInfo;
	}

	public String getSpecsInfo() {
		return specsInfo == null? "": specsInfo.substring(specsInfo.indexOf(">") +1, specsInfo.indexOf("</div>"));
	}

	public void setSpecsInfo(String specsInfo) {
		this.specsInfo = specsInfo;
	}

	public String getShipInfo() {
		return shipInfo == null? "": shipInfo.substring(shipInfo.indexOf(">") +1, shipInfo.indexOf("</div>"));
	}

	public void setShipInfo(String shipInfo) {
		this.shipInfo = shipInfo;
	}

	public String getPdfInfo() {
		return pdfInfo == null? "": pdfInfo.substring(pdfInfo.indexOf(">") +1, pdfInfo.indexOf("</div>"));
	}

	public void setPdfInfo(String pdfInfo) {
		this.pdfInfo = pdfInfo;
	}

	public String getProductResourceCatalog() {
		return productResourceCatalog;
	}

	public void setProductResourceCatalog(String productResourceCatalog) {
		this.productResourceCatalog = productResourceCatalog;
	}

	public String getProductResourceCare() {
		return productResourceCare;
	}

	public void setProductResourceCare(String productResourceCare) {
		this.productResourceCare = productResourceCare;
	}

	public String getProductResourceSpecification() {
		return productResourceSpecification;
	}

	public void setProductResourceSpecification(String productResourceSpecification) {
		this.productResourceSpecification = productResourceSpecification;
	}

	public List<String> getProductResources() {
		return productResources;
	}

	public void setProductResources(List<String> productResources) {
		this.productResources = productResources;
	}
	
}
