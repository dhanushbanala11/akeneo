package com.striketru.bc2akeneo.reader;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.transformer.BC2PIMTransformer;
import com.striketru.bc2akeneo.writer.PIMWriter;
import com.striketru.bc2akeneo.writer.WriterData;
import com.striketru.conn.base.Reader;

public class BigCommReader extends Reader {
    private static final Logger LOGGER = LogManager.getLogger(BigCommReader.class);

	private static BigCommAPI bigCommAPI = null;
	private static ObjectMapper objMapper = new ObjectMapper();
	private static BC2PIMTransformer bc2pimTranformer = new BC2PIMTransformer();
	private static PIMWriter pimWriter;
	


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
			boolean isNotPageRead = true;
			
			if (isNotPageRead) {
				List<Object> dataTemp =  bigCommAPI.getData("9147", "2864", "440", "375", "233"); //"9147", "2864", "440", "375", "233" 
				executeProductPage(dataTemp);
			} else {
				int pageCount = bigCommAPI.getDataPageCount();
				pageCount = 50;
				List<Object> data = null;
				for (int i= 1; i <= pageCount; i++) {
					data =  bigCommAPI.getDataByPage(i);
					executeProductPage(data);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeProductPage(List<Object> data){
		for (Object productData : data) {
			Map<String, Object> productDataMap = objMapper.convertValue(productData, Map.class);
			ReaderData reader = new ReaderData(productData);
			WriterData writerData = bc2pimTranformer.execute(reader);
			pimWriter.execute(writerData);
		}

	}


	



}
