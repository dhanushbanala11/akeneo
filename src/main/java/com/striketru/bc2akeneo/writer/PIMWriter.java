package com.striketru.bc2akeneo.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.conn.base.Writer;
import com.striketru.pim.model.MediaJson;
import com.striketru.pim.model.ProductJson;
import com.striketru.pim.util.PIMRequestUtil;

public class PIMWriter extends Writer<WriterData> implements PIMRequestUtil {
    private static final Logger LOGGER = LogManager.getLogger(PIMWriter.class);
    private static final Logger FILE_LOGGER = LogManager.getLogger("ftpFile");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
					ProductJson prodJson = writeData.getBaseProduct();
					for (String request: requestList)  {
						if (StringUtils.isNotEmpty(request)){
							String response = productapi.upsertMutipleProducts(request);
							writeData.getReport().addResponse(prodJson.getIdentifier()+"--"+response);
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
			for (MediaJson mediaJson: writeData.getImages()) {
				writeImagetoPIM(mediaJson);
			}
		}
		if (isLoadDocs) { 
			for (MediaJson mediaJson: writeData.getDocuments()) {
				String inputFilename = mediaJson.getUrl().substring(mediaJson.getUrl().lastIndexOf("/") + 1);
				if (inputFilename.contains("?")) {
					inputFilename = inputFilename.substring(0, inputFilename.indexOf("?"));
				}
				FILE_LOGGER.info(String.format("%s|%s|%s|%s", mediaJson.getIdentifier(), mediaJson.getAttribute_code(),  inputFilename, mediaJson.getUrl()));
//				writeImagetoPIM(mediaJson);
			}
		}
	}
	
	public String createAssociationRequest(ProductJson productJson, Map<String, ProductJson> optionsProduct) {
    	StringBuilder strbuild = new StringBuilder("{");
    	strbuild.append(createKeyValueJson("identifier", productJson.getIdentifier()));
    	strbuild.append(",").append(createKeyValueJson("family", productJson.getFamily()));
		List<String> swatchOptions = new ArrayList<>();
		List<String> dropDownOptions = new ArrayList<>();
		for (ProductJson optionProd: optionsProduct.values()) {
			if(optionProd.getFamily().equals("options_swatches")) {
				swatchOptions.add(optionProd.getIdentifier());
			}else {
				dropDownOptions.add(optionProd.getIdentifier());
			}
		}

		if(swatchOptions.size() >0 || dropDownOptions.size() >0) {
			strbuild.append(",").append("\"associations\": {");
			if(swatchOptions.size() >0) {
				strbuild.append("\"options_swatches\": { ");
				strbuild.append(createKeyValueListJson("products", swatchOptions));
				strbuild.append("}");
			}
			if(dropDownOptions.size() >0) {
				if(swatchOptions.size() >0) {
					strbuild.append(",");
				}
				strbuild.append("\"options_dropdown\": { ");
				strbuild.append(createKeyValueListJson("products", dropDownOptions));
				strbuild.append("}");
			}
			strbuild.append("}");
		}
		strbuild.append("}");
    	return strbuild.length() > 2 ? strbuild.toString() : null;
	}
	
	public void writeImagetoPIM(MediaJson imageJson) {
		String destinationPath = downloadFileToTempFolder(imageJson.getUrl(), getTempFolder());
		try {
			productapi.createMediafile(destinationPath, createMediaProductJson(imageJson.getIdentifier(), imageJson.getAttribute_code(), 
					imageJson.getLocale(), imageJson.getScope()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeImagetoPIM(String imageUrl, String identifier, String attribute,  String locale, String scope) {
		String destinationPath = downloadFileToTempFolder(imageUrl, getTempFolder());
		try {
			productapi.createMediafile(destinationPath, createMediaProductJson(identifier, attribute, locale, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeJsonToFile(Object productData, String bcId, boolean isPermanentlyHidden) {
		String destinationPath = getPdfFolder();
		String hiddenFolderPath = "/hidden";
		String visibleFolderPath = "/visible";
		if(isPermanentlyHidden)
			destinationPath = destinationPath+hiddenFolderPath;
		else
			destinationPath = destinationPath+visibleFolderPath;
		try {
			FileWriter file = new FileWriter(destinationPath+"/"+bcId+".json");
			gson.toJson(productData, file);
			file.close();
		}catch (Exception e) {
			LOGGER.error(bcId+"|"+e.getMessage());
		}
	}


}
