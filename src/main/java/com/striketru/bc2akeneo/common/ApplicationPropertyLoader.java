/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.striketru.bc2akeneo.model.Config;
import com.striketru.bc2akeneo.util.LoaderProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author anepolean
 */

@PropertySource("file:bc2akeneo.properties")
public class ApplicationPropertyLoader  {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationPropertyLoader.class);
    /**
     * Map to save the properties of the application.
     */
    private final Map<String, String> appProperties;
    private final Config config;



    /**
     * Create a new reader with the properties of the application.
     */
    public ApplicationPropertyLoader() {
        appProperties = loadAppProperties();
        config = getYamlProperties();
    }

    /**
     * Returns the properties of the application.
     *
     * @return a map of properties.
     */
    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    public Config getConfig() {
        return config;
    }

    /**
     * Adds properties to map applicationProperties.
     */
    private Map<String, String> loadAppProperties() {
        Map<String, String> appProperties = new HashMap<>();
//        try {
//            Properties propertiesPidam = LoaderProperties.getInstance().readFile("bc2akeneo.properties");
        	appProperties.put("pimurl", "https://authenteak-production.cloud.akeneo.com");
        	appProperties.put("pimclient", "1_18kxcawvycm8ckgso84osccskgg8480w8ogsc004cooww48w0w");
        	appProperties.put("pimsecret", "52ch16awca04cks4scg0ks4oc40s4ocgww0oks00kwskksg8o4");
        	appProperties.put("pimusername", "api_8251");
        	appProperties.put("pimpassword", "a00ec065c");
//            propertiesPidam.forEach((key, value) -> appProperties.put(key.toString(), value.toString()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.error("Exceptions in Application Property file: " +e);
//        }
        return appProperties;
    }

    public Config getYamlProperties() {
        String filename = System.getProperty("env") + appProperties.get("config_file");
        Config config = null;
        LOGGER.debug("Configuration file: {" + filename + "}");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            config = mapper.readValue(new File(filename), Config.class);
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
