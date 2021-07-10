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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author anepolean
 */
public class ReaderApplicationProperties {
    /**
     * Constant for path of application properties.
     */
    private static final String PIDAM_PROPERTIES = "erp2akeneo.properties";
    /**
     * Map to save the properties of the application.
     */
    private final Map<String, String> applicationProperties;

    /**
     * Create a new reader with the properties of the application.
     */
    protected ReaderApplicationProperties() {
        applicationProperties = new HashMap<>();
        addPidamProperties();
    }

    /**
     * Returns a new instance of ReaderApplicationProperties.
     *
     * @return a ReaderApplicationProperties.
     */
    public static ReaderApplicationProperties getInstance() {
        return new ReaderApplicationProperties();
    }

    /**
     * Returns the properties of the application.
     *
     * @return a map of properties.
     */
    public Map<String, String> getAppProperties() {

        return applicationProperties;
    }

    /**
     * Adds properties for the pidam to map applicationProperties.
     */
    private void addPidamProperties() {
        Properties propertiesPidam = LoaderProperties.getInstance().readFile(PIDAM_PROPERTIES);
        propertiesPidam.forEach((key, value) -> applicationProperties.put(key.toString(), value.toString()));
    }
    
    public Config getYamlProperties(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            String environment = applicationProperties.get("environment");
            Config config = mapper.readValue(new File(environment+"_config.yml"), Config.class);
            return config;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
