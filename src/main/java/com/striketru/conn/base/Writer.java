package com.striketru.conn.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.conn.base.model.BaseData;
import com.striketru.conn.base.model.Result;
import com.striketru.pim.model.ProductJson;

public abstract class Writer<W extends BaseData> {
    private static final Logger LOGGER = LogManager.getLogger(Writer.class);
	public static final int MAX_PRODUCT_COUNT = 50;
	public static final String TEMP_FOLDER = getTempFolderPath();
	List<Result> result;
	
	public abstract void execute(W writeData);
	
	
	public List<String> getRequestList(List<ProductJson> productJsonList) {

		int count = 0;
		List<String> optionsList = new ArrayList<>();
		StringBuilder strbuild = new StringBuilder("");
		for (ProductJson productJson : productJsonList) {
			if (count == MAX_PRODUCT_COUNT) {
				optionsList.add(strbuild.toString());
				strbuild = new StringBuilder("");
				count = 0;
			}
			strbuild.append(productJson.createRequest()).append("\n");
			count++;
		}
		if (strbuild.length() > 2) { 
			optionsList.add(strbuild.toString());
		}
		return optionsList;
	}

    protected static String getTempFolderPath()  {
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
    
    protected static String downloadFileToTempFolder(String imageUrl, String tmpFolderPath)    {
		String inputFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		if (inputFilename.contains("?")) {
			inputFilename = inputFilename.substring(0, inputFilename.indexOf("?"));
		} 
		LOGGER.debug(inputFilename);
		String destinationPath = tmpFolderPath+File.separator+inputFilename;
		LOGGER.debug(destinationPath);
        try(InputStream in = new URL(imageUrl).openStream()){
            Files.copy(in, Paths.get(destinationPath));
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return destinationPath;
    }
}
