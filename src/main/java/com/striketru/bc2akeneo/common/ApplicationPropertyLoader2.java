/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 * @author anepolean
 */

//@PropertySource("file:bc2akeneo.properties")
public class ApplicationPropertyLoader2  {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationPropertyLoader2.class);
    /**
     * Map to save the properties of the application.
     */
    private final Map<String, String> appProperties;



    /**
     * Create a new reader with the properties of the application.
     */
    public ApplicationPropertyLoader2() {
        appProperties = loadAppProperties();
    }

    /**
     * Returns the properties of the application.
     *
     * @return a map of properties.
     */
    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    /**
     * Adds properties to map applicationProperties.
     */
    private Map<String, String> loadAppProperties() {
        Map<String, String> appProperties = new HashMap<>();
        try {
            Properties propertiesPidam = readFile("bc2akeneo.properties");
            propertiesPidam.forEach((key, value) -> appProperties.put(key.toString(), value.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exceptions in Application Property file: " +e);
        }
        return appProperties;
    }

    /**
     * Loads a property file.
     *
     * @param path of properties.
     * @return the properties.
     */
    public Properties readFile(String path) {
        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(path);            
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
    
    @SuppressWarnings("unchecked")
	public Map<String, Object> getYamlProperties() {
        String filename = System.getProperty("env") + "-config.yml";
        Map<String, Object> config = null;
        LOGGER.debug("Configuration file: {" + filename + "}");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            config = mapper.readValue(new File(filename), Map.class);
        } catch (IOException  e) {
            LOGGER.error("Not able to read the Config " + filename + " file");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exceptions in Config file reading " + e);
        }
        return config;
    }

    public String get(String field){
        return appProperties.get(field);
    }


}
