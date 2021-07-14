package com.striketru.bc2akeneo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.api.BcAPI;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.bc2akeneo.common.ApplicationPropertyLoader;
import com.striketru.bc2akeneo.model.WriteResult;
import com.striketru.bc2akeneo.util.RequestUtil;

//@SpringBootApplication
public class Bc2akeneoApplication {

	
	RequestUtil akeneoUtil = new RequestUtil();
	ObjectMapper mapper = new ObjectMapper();
	String tempFolderPath = getTempFolderPath();
	ProductAPI productapi = null;
	BcAPI bcApi = new BcAPI();

	public Bc2akeneoApplication(){
		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		productapi = new ProductAPI(appProp.getAppProperties());
	}
	
	public void execute(){
		
		Map<String, Object> bcResp = null;
		System.out.println("Application Properties : " + System.getProperty("APP_PROP"));
		System.out.println("** Connecting to BC **");

		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		ProductAPI productapi = new ProductAPI(appProp.getAppProperties());
		List<String> createdOptions = new ArrayList<String>();
		try {
			List<WriteResult> results = new ArrayList<>(); 
			boolean isNotPageRead = true;
			
			if (isNotPageRead) {
				List<Object> dataTemp =  getBcData("2864"); //9147, 2864
				executeProductPage(dataTemp, results, createdOptions);
			} else {
				int pageCount = getBcDataPageCount();
				pageCount = 30;
				List<Object> data = null;
				for (int i= 1; i <= pageCount; i++) {
					data =  getBcData(i);
					executeProductPage(data, results, createdOptions);
				}
			}
			
			displayResults(results);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void executeProductPage(List<Object> data, List<WriteResult> results, List<String> createdOptions){
		ObjectMapper oMapper = new ObjectMapper();
		for (Object productData : data) {
			Map<String, Object> productDataMap = oMapper.convertValue(productData, Map.class);
			try {
				WriteResult result = new WriteResult(productDataMap.get("sku").toString());
				List<String> optionProductRequest = akeneoUtil.createUpdateOptionProducts(productDataMap.get("sku").toString(), productDataMap, result, createdOptions);
				boolean isOptionProductsExists = false;
				for (String request: optionProductRequest)  {
					System.out.println(request);
					if (StringUtils.isNotEmpty(request)){
						isOptionProductsExists = true;
						String response = productapi.upsertMutipleProducts(request);
						result.setOptionsResponse(response);
					}
				}
				String baseProductRequest = akeneoUtil.createUpdateBaseProduct(productDataMap, isOptionProductsExists);
				System.out.println("Request : " + baseProductRequest);
				productapi.upsertProductBySku(productDataMap.get("sku").toString(), baseProductRequest);
//				String primaryImageUrl = (String)((Map<String, Object>)productDataMap.get("primary_image")).get("url_standard");
//				imageWritetoPIM(primaryImageUrl, tempFolderPath, productDataMap.get("sku").toString(),"primary_image", null, null);
				
				String priceRequest = akeneoUtil.createProductPrices(productDataMap.get("sku").toString(), productDataMap, result);
				productapi.upsertMutipleProducts(priceRequest);
				
				results.add(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void displayResults(List<WriteResult> results) throws IOException {
		FileWriter myWriter = new FileWriter("akeneo_logs.txt");	
//		String content = "**** start **** \n";
//		for (WriteResult result: results) {
//			content += result.countString() + "\n";
//			System.out.println(result.countString());
//		}
//		content += "**** End **** \n";
		String content = "";
		for (WriteResult result: results) {
			content += "\n SKU : "+ result.getSku()+"|";
			System.out.println("SKU : "+ result.getSku());
			content +=  "Options Product: count="+result.getOptionsCount()+"|";
			System.out.println("Options_Product_count: "+result.getOptionsCount());
			content += result.getOptionsResponse()+"|";
			System.out.println(result.getOptionsResponse());
			content += "price_count: "+result.getPriceCount()+"|";
			System.out.println("price: count="+result.getPriceCount());
			content += result.getPriceResponse()+"|";
			System.out.println(result.getPriceResponse());
			content += "image: count="+result.getImageCount()+"|";
			System.out.println("image: count="+result.getImageCount());
			
			content += "Variant :"+ result.isVariants()+"|";
			content += "Modifiers :"+ result.isModifiers()+"|";
			
			content += result.getImageResponse();
			System.out.println(result.getImageResponse());
		}
			myWriter.write(content);
			myWriter.close();
		
	}
	
	public int getBcDataPageCount() throws IOException {
		BcAPI bcApi = new BcAPI();
		String pages = "";
		String url = "https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options";
		Content bcResponse = bcApi.get(url);
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		if (bcResp.get("meta") != null) {
			Map<String, Object> meta = (Map<String, Object>)bcResp.get("meta");
			Map<String, Object> pagination = (Map<String, Object>)meta.get("pagination");
			pages = pagination.get("total_pages").toString();
			System.out.println(pages);
		}
		System.out.println(bcResp.get("meta"));
		return  StringUtils.isNotEmpty(pages) ? Integer.valueOf(pages) : 0;
	}
	
	
	
	/**
	 * 
	 * @throws IOException
	 */
	public List<Object> getBcData(int pageNum) throws IOException {
		String url = "https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options&page="+pageNum+"&limit=10";
		Content bcResponse = bcApi.get(url);
		Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
		System.out.println(bcResp.get("data"));
		return  (List<Object>) bcResp.get("data");
	}
	

	public List<Object> getBcData(String... ids ) throws IOException {
		List<Object> listObj = new ArrayList<>();
		for (String id : ids) {
			String url = "https://api.bigcommerce.com/stores/r14v4z7cjw/v3/catalog/products/"+ id + "?include=variants,images,custom_fields,bulk_pricing_rules,primary_image,modifiers,options";
			Content bcResponse = bcApi.get(url);
			Map<String, Object> bcResp = mapper.readValue(bcResponse.toString(), Map.class);
			System.out.println(bcResp.get("data"));
			listObj.add(bcResp.get("data"));
		}
		return listObj;
	}
	
	

	public void executeImageDownload(){
//		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg";
		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg?c=2";
		imageWritetoPIM(imageUrl, tempFolderPath, "PP-R1043-X","primary_image", null, null);
		

	}

	public void imageWritetoPIM(String imageUrl, String tempFolderPath, String identifier, String attribute,  String locale, String scope) {
		String destinationPath = downloadFileToTempFolder(imageUrl, tempFolderPath);
		try {
			productapi.createMediafile(destinationPath, akeneoUtil.createMediaProductJson(identifier, attribute, locale, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private String downloadFileToTempFolder(String imageUrl, String tmpFolderPath)    {
		String inputFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		if (inputFilename.contains("?")) {
			inputFilename = inputFilename.substring(0, inputFilename.indexOf("?"));
		} 
		System.out.println(inputFilename);
		String destinationPath = tmpFolderPath+File.separator+inputFilename;
		System.out.println(destinationPath);
        try(InputStream in = new URL(imageUrl).openStream()){
            Files.copy(in, Paths.get(destinationPath));
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return destinationPath;
    }
    
    private String getTempFolderPath()  {
        File tmpFile = new File(FileSystems.getDefault().getPath("").toAbsolutePath().toString().concat(File.separator+"temp"));
        if(tmpFile.exists()) {
			try {
				FileUtils.cleanDirectory(tmpFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else {
            tmpFile.mkdir();
        }
        return tmpFile.getAbsolutePath();
    }
	
	public static void main(String[] args) throws Exception {
		new Bc2akeneoApplication().execute();
//		new Bc2akeneoApplication().executeImageDownload();
	}

}
