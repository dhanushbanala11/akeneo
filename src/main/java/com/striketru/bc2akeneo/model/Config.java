/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author anepolean
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    private String csvfilepath;
    private String csvfilepattern;
    private String singlesku;
    private Map<String, String> mapcsvtopim;

    public String getCsvfilepath() {
        return csvfilepath;
    }

    public void setCsvfilepath(String csvfilepath) {
        this.csvfilepath = csvfilepath;
    }

    public String getCsvfilepattern() {
        return csvfilepattern;
    }

    public void setCsvfilepattern(String csvfilepattern) {
        this.csvfilepattern = csvfilepattern;
    }

    public String getSinglesku() {
        return singlesku;
    }

    public void setSinglesku(String singlesku) {
        this.singlesku = singlesku;
    }

    public Map<String, String> getMapcsvtopim() {
        return mapcsvtopim;
    }

    public void setMapcsvtopim(Map<String, String> mapcsvtopim) {
        this.mapcsvtopim = mapcsvtopim;
    }

    public String getSKUField(){
        String skufield = null;
        for (String key : getMapcsvtopim().keySet()) {
            if (("identifier").equals(getMapcsvtopim().get(key))) {
                skufield = key;
            }
        }
        return skufield;
    }

    @Override
    public String toString() {
        return "Config{" +
                "csvfilepath='" + csvfilepath + '\'' +
                ", csvfilepattern='" + csvfilepattern + '\'' +
                ", singlesku='" + singlesku + '\'' +
                ", mapcsvtopim=" + mapcsvtopim +
                '}';
    }

}
