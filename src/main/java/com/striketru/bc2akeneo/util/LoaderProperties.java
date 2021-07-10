/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * LoaderProperties class.
 *
 * @author anepolean
 * @version 0.0.1
 */
public class LoaderProperties {

    /**
     * Constructor protected.
     */
    protected LoaderProperties() {
    }

    /**
     * Initialize a new LoaderProperties.
     *
     * @return a LoaderProperties.
     */
    public static LoaderProperties getInstance() {
        return new LoaderProperties();
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
}
