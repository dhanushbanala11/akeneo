package com.striketru.bc2akeneo;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.striketru.bc2akeneo.reader.BigCommAPI;
import com.striketru.bc2akeneo.reader.BigCommReader;
import com.striketru.bc2akeneo.transformer.BigcommerceTransformer;

 public class Bc2akeneoApplication 
 {
	
public static void main(String[] args) throws Exception
{
	
		new Bc2akeneoApplication().execute();
	
	}
	
	/*
	
	public static void execute() throws IOException {
		BigCommAPI bigcomm= new BigCommAPI("fa2e43fed9e8f12eaff4539926f8779bd9bf57ce8d03b5d3d9f965e78d672bfd","340kbi7pj2yitvrne21nxoq39ixs3au","vee1fkq9r9","https://api.bigcommerce.com");
	
		//BigCommReader bigcomm= new BigCommReader();
		List<Object> productData = bigcomm.getData("65074");
		BigcommerceTransformer transformer = new BigcommerceTransformer();
		String ProductData = transformer.transformData(productData);
		//System.out.println(bigcomm.getData("65074"));
		
		String ow = new ObjectMapper().writer().writeValueAsString(bigcomm.getData("65074"));
		System.err.println(ow);
		System.err.println(productData);
		
	//	List<Object> dataTemp=bigcomm.getData();
		//System.out.println(bigcomm);
		//System.err.println("HI");
				
	}
	
*/
	 
	
		public void execute() throws IOException 

		{
			BigCommAPI bigcomm= new BigCommAPI("fa2e43fed9e8f12eaff4539926f8779bd9bf57ce8d03b5d3d9f965e78d672bfd","340kbi7pj2yitvrne21nxoq39ixs3au","vee1fkq9r9","https://api.bigcommerce.com");
			 Gson gson = new GsonBuilder().setPrettyPrinting().create();
			List<Object> productData = bigcomm.getData("65073","65077");	
			
			BigcommerceTransformer transformer = new BigcommerceTransformer();
			String ProductData = transformer.transformData(productData);
			
			System.err.println(ProductData);
		    // String json = gson.toJson(productData);
		     // System.out.println(json);
			
/*
		try {
			PIMMinifyConnector aKeneoMinifyAPI;
			aKeneoMinifyAPI = new PIMMinifyConnector("test.com", "http://192.168.29.217",
					"h9r74vtrazkk4wkgk4cs8wo84848g88co4c0woo0sw8osksgs",
					"5_343cz2aq708wkcwko408484os4oc80o4s0c00ocwk0gg00k80k", "db_connection_7609", "cc05a9126");
			 String token=aKeneoMinifyAPI.getAuthService().getServiceToken();
			 
			 
			 System.err.println("Access Token"+token);
			// String request= {"identifier":"13710067"};
			 String request= "{\"identifier\":\"13710067\",\"values\":{\"description\":[{\"locale\":\"en_US\",\"data\":\"<p>Nataliya shoulder bag, paired with outfit-TEST.Akeneo<br></p>\",\"scope\":\"ecommerce\"}]}}";
			ProductAPI productapi=new ProductAPI("test.com");
			 //productapi.createProductAsJson(request);
			 productapi.updateProductAsJson("13710067",request);
			
			 
		} catch (Exception e) {
			System.err.println(e);
		}
		*/
		
}
	
}