package com.striketru.bc2akeneo.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.conn.base.Writer;
import com.striketru.pim.model.ImageJson;
import com.striketru.pim.model.ProductJson;
import com.striketru.pim.util.PIMRequestUtil;

public class PIMWriter extends Writer<WriterData> implements PIMRequestUtil {
    private static final Logger LOGGER = LogManager.getLogger(PIMWriter.class);

	private ProductAPI productapi = null;
	
	
	public PIMWriter(Map<String, String> appProp ) {
		this.productapi = new ProductAPI(appProp.get("pim_url"), appProp.get("pim_client"),appProp.get("pim_secret"), appProp.get("pim_username"), appProp.get("pim_password"));
		
	}

	@Override
	public void execute(WriterData writeData) {
		try {
			
			String baseProductRequest = writeData.getBaseProduct().createRequest();
			String baseProductResponse = productapi.upsertMutipleProducts(baseProductRequest);
			writeData.getReport().addResponse(baseProductResponse);
			if (baseProductResponse.contains(":204") || baseProductResponse.contains(":201")) {
				if ( writeData.getOptionsProduct().size() > 0) {
					Map<String, ProductJson> optionsProduct = writeData.getOptionsProduct();
					List<ProductJson> prodJsonList = new ArrayList<>(optionsProduct.values());
					writeData.getReport().setWriteProductOptionsCount(prodJsonList.size());
					List<String> requestList =  getRequestList(prodJsonList);
					for (String request: requestList)  {
						if (StringUtils.isNotEmpty(request)){
							String response = productapi.upsertMutipleProducts(request);
							writeData.getReport().addResponse(response);
						}
					}

					String baseProductAssociationRequest =  createAssociationRequest(writeData.getBaseProduct(), writeData.getOptionsProduct());
					String baseProductAssociationResponse = productapi.upsertMutipleProducts(baseProductAssociationRequest);
					writeData.getReport().addResponse(baseProductAssociationResponse);
					requestList =  getRequestList((List<ProductJson>)writeData.getPrice());
					for (String request: requestList)  {
						if (StringUtils.isNotEmpty(request)){
							String response = productapi.upsertMutipleProducts(request);
							writeData.getReport().addResponse(response);
						}
					}
				}
			}
			writeData.getReport().writeLog();
			writeData.getReport().writeResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void executeMediaFiles(WriterData writeData, boolean isLoadImage, boolean isLoadDocs) {
		if (isLoadImage) { 
			for (ImageJson imageJson: writeData.getImages()) {
				writeImagetoPIM(imageJson);
			}
		}
	}
	
	public String createAssociationRequest(ProductJson productJson, Map<String, ProductJson> optionsProduct) {
    	StringBuilder strbuild = new StringBuilder("{");
    	strbuild.append(createKeyValueJson("identifier", productJson.getIdentifier()));
    	strbuild.append(",").append(createKeyValueJson("family", productJson.getFamily()));
		List<String> prodOptions = new ArrayList<>();
		for (ProductJson optionProd: optionsProduct.values()) {
			prodOptions.add(optionProd.getIdentifier());
		}
		if(prodOptions.size() >0) {
			strbuild.append(",").append("\"associations\": { \"options\": { ");
			strbuild.append(createKeyValueListJson("products", prodOptions));
			strbuild.append("}}");
		}
		strbuild.append("}");
    	return strbuild.length() > 2 ? strbuild.toString() : null;	
	}
	
	public void writeImagetoPIM(ImageJson imageJson) {
		String destinationPath = downloadFileToTempFolder(imageJson.getUrl(), TEMP_FOLDER);
		try {
			productapi.createMediafile(destinationPath, createMediaProductJson(imageJson.getIdentifier(), imageJson.getAttribute_code(), 
					imageJson.getLocale(), imageJson.getScope()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeImagetoPIM(String imageUrl, String identifier, String attribute,  String locale, String scope) {
		String destinationPath = downloadFileToTempFolder(imageUrl, TEMP_FOLDER);
		try {
			productapi.createMediafile(destinationPath, createMediaProductJson(identifier, attribute, locale, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
