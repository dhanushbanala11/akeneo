package com.striketru.bc2akeneo.reader;

import java.util.List;

import com.striketru.bc2akeneo.transformer.BigcommerceTransformer;

import java.io.IOException;

public class BigCommReader {

	
		 
		
		public void execute() throws IOException 
		{
			BigCommAPI bigcomm= new BigCommAPI("fa2e43fed9e8f12eaff4539926f8779bd9bf57ce8d03b5d3d9f965e78d672bfd","340kbi7pj2yitvrne21nxoq39ixs3au","vee1fkq9r9","https://api.bigcommerce.com");
			List<Object> productData = bigcomm.getData("65074");
			
			BigcommerceTransformer transformer = new BigcommerceTransformer();
			String ProductData = transformer.transformData(productData);
			
			System.err.println(ProductData);
			
		}
		
	}

