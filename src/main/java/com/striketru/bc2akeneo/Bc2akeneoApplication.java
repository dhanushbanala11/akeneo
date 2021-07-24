package com.striketru.bc2akeneo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.striketru.bc2akeneo.api.ProductAPI;
import com.striketru.bc2akeneo.common.ApplicationPropertyLoader;
import com.striketru.bc2akeneo.reader.BigCommReader;
import com.striketru.bc2akeneo.util.RequestUtil;

public class Bc2akeneoApplication {

	private BigCommReader reader;
	
	RequestUtil akeneoUtil = new RequestUtil();
	ObjectMapper mapper = new ObjectMapper();
	String tempFolderPath = getTempFolderPath();
	ProductAPI productapi = null;

	public Bc2akeneoApplication() throws IOException{
		ApplicationPropertyLoader appProp = new ApplicationPropertyLoader();
		reader = new BigCommReader(appProp.getAppProperties());
	}
	
	public void execute() throws IOException{
		reader.execute();
	}
	
	public void executeImageDownload(){
//		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg";
		String imageUrl = "https://cdn11.bigcommerce.com/s-r14v4z7cjw/products/9147/images/83114/PP-R1043-97-X_main__00695.1624995417.500.500.jpg?c=2";
		imageWritetoPIM(imageUrl, tempFolderPath, "PP-R1043-X","primary_image", null, null);
		

	}

	public void imageWritetoPIM(String imageUrl, String tempFolderPath, String identifier, String attribute,  String locale, String scope) {
		String destinationPath = downloadFileToTempFolder(imageUrl, tempFolderPath);
		try {
			productapi.createMediafile(destinationPath, RequestUtil.createMediaProductJson(identifier, attribute, locale, scope));
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
