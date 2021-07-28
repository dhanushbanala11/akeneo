package com.striketru.bc2akeneo;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.bc2akeneo.common.ApplicationPropertyLoader;
import com.striketru.bc2akeneo.reader.BigCommReader;

public class Bc2akeneoApplication {

	private BigCommReader reader;
	
	ObjectMapper mapper = new ObjectMapper();
	ProductAPI productapi = null;

	public Bc2akeneoApplication() throws IOException{
		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		reader = new BigCommReader(appProp.getAppProperties());
	}
	
	public void execute() throws IOException{
		reader.execute();
	}
	
	public static void main(String[] args) throws Exception {
		new Bc2akeneoApplication().execute();
	}
	

	
}
