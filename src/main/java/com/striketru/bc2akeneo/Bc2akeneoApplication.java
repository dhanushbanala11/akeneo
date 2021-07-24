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
	
//	public void executeImageDownload(){
////		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg";
//		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg?c=2";
//		imageWritetoPIM(imageUrl, tempFolderPath, "PP-R1043-X","primary_image", null, null);
//		
//
//	}

	public static void main(String[] args) throws Exception {
		new Bc2akeneoApplication().execute();
//		new Bc2akeneoApplication().executeImageDownload();
	}
}
