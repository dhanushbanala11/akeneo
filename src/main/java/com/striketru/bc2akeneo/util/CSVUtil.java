package com.striketru.bc2akeneo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.striketru.bc2akeneo.model.PIMValue;

public class CSVUtil {
	
	public enum CSV_FILE_TYPE{ATTRIBUTE_OPTION, FAMILIES}

	public Map<String, String> getPropertyFromCSV(String csvfileName, CSV_FILE_TYPE csvFileType) {
		ClassLoader classLoader = getClass().getClassLoader();
		Map<String, String> record =  new HashMap<>();
        try (InputStream inputStream = classLoader.getResourceAsStream(csvfileName);
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {		
			String line = reader.readLine(); 
			while (line != null) {
				if (csvFileType == CSV_FILE_TYPE.ATTRIBUTE_OPTION) { 
					String[] recordLine = line.split(";");
					if (recordLine.length >=3) { 
						recordLine[1] = recordLine[1].replace("\"", "");
						record.put(recordLine[2].trim()+"-"+recordLine[1].trim(), recordLine[0].trim());
					}
				} else if (csvFileType == CSV_FILE_TYPE.FAMILIES) {
					String[] recordLine = line.split(",");
					if (recordLine.length >=2) { 
						recordLine[1] = recordLine[1].replace("\"", "");
						record.put(recordLine[0].trim(), recordLine[1].trim());
					}
				}
				line = reader.readLine(); 
			} 
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println(record.toString());
		return record;
	}
	
	public Map<String, PIMValue> getPropertyFromCSV(String csvfileName) {
		ClassLoader classLoader = getClass().getClassLoader();

		Map<String, PIMValue> record =  new HashMap<>();
        try (InputStream inputStream = classLoader.getResourceAsStream(csvfileName);
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {		
			String line = reader.readLine(); 
			while (line != null) {
				String[] recordLine = line.split(",");
				if (recordLine.length >=3) {
					String metric = null;
					if(recordLine.length == 4) {
						metric = recordLine[3].trim();
					}
					record.put(recordLine[0].trim(), new PIMValue(recordLine[1].trim(), recordLine[2].trim(), metric));
				}
				line = reader.readLine(); 
			} 
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return record;
	}
	
	
}
