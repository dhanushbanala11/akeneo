package com.striketru.bc2akeneo.reader;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.transformer.BC2PIMTransformer;
import com.striketru.bc2akeneo.writer.PIMWriter;
import com.striketru.bc2akeneo.writer.WriterData;
import com.striketru.conn.base.Reader;

public class BigCommReader extends Reader {
	public static final Logger LOGGER = LogManager.getLogger(BigCommReader.class);

	public static BigCommAPI bigCommAPI = null;
	public static BC2PIMTransformer bc2pimTranformer = new BC2PIMTransformer();
	public static PIMWriter pimWriter;
	public static final int LOAD_VISIBLE_COUNT = 500;
	public static int currentVisibleCount = 0;

	public BigCommReader(Map<String, String> appProp ){
		String url = appProp.get("bigComm_url");
		url = url.replace("$$bigComm_storeHash##", appProp.get("bigComm_storeHash"));
		LOGGER.info(url);
		bigCommAPI = new BigCommAPI(appProp.get("bigComm_authClient"), appProp.get("bigComm_authToken"), appProp.get("bigComm_storeHash"), url);
		pimWriter = new PIMWriter(appProp);
	}

	@Override
	public void execute() {
		try {
			boolean isNotPageRead = false;
			boolean isLoadImage = false;
			boolean isLoadDocs = false;
			if (isNotPageRead) {
				List<Object> dataTemp =  bigCommAPI.getData("9243"); //"9147", "2864", "440", "375", "233" 
				executeProductPage(dataTemp, isLoadImage, isLoadDocs);
			} else {
				int pageCount = bigCommAPI.getDataPageCount();
				//pageCount = 50; // temp statement
				List<Object> data = null;
				for (int i= 1; i <= pageCount; i++) {
					data =  bigCommAPI.getDataByPage(i);
					boolean isContinue = executeProductPage(data, isLoadImage, isLoadDocs);
					if (!isContinue) {
						break;
					}
					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean executeProductPage(List<Object> data, boolean isLoadImage, boolean isLoadDocs){
		for (Object productData : data) {
			ReaderData reader = new ReaderData(productData);
			if (isLoadNextData(reader)) {
				WriterData writerData = bc2pimTranformer.execute(reader);
				pimWriter.execute(writerData);
				pimWriter.executeMediaFiles(writerData, isLoadImage, isLoadDocs);
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean isLoadNextData(ReaderData reader) {
		if (currentVisibleCount < LOAD_VISIBLE_COUNT) {
			if (reader.getProduct().get("is_visible") != null && reader.getProduct().get("is_visible").toString().equalsIgnoreCase("true")  ) {
				currentVisibleCount++;
			}
			return true;
		}
		return false;
	}

	



}
