package com.striketru.bc2akeneo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValueField {

    private String locale = null;
    private String scope = null;
    private String data = null;
    private String unit = null;
    private Map<String, String> dataConversion;

    public ValueField(String pimfield) {
        if (pimfield.contains("(")){
            String splprop = getSubstring(pimfield, "(", ")");
            String[] strArr = splprop.split("\\|");
            for (String fieldValue : strArr){
                if (fieldValue.startsWith("MAP")) {
                    dataConversion(fieldValue);
                } else {
                    String[] keyValArr = fieldValue.split("=");
                    if ("locale".equals(keyValArr[0])) {
                        locale = keyValArr[1];
                    } else if ("scope".equals(keyValArr[0])) {
                        scope = keyValArr[1];
                    } else if ("unit".equals(keyValArr[0])) {
                        unit = keyValArr[1];
                    }
                }
            }
            data = pimfield.substring(0, pimfield.indexOf("("));
        } else {
            data = pimfield;
        }

    }

    public void dataConversion(String mapdata){
        String mapProp = getSubstring(mapdata, "MAP[", "]");
        if (mapProp != null) {
            dataConversion = new HashMap<>();
            String[] mapArr = mapProp.split("\\;");
            for (String fieldValue : mapArr){
                String[] keyval = fieldValue.split("\\=");
                dataConversion.put(keyval[0], keyval[1]);
            }
        }
    }



    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, String> getDataConversion() {
        return dataConversion;
    }

    public void setDataConversion(Map<String, String> dataConversion) {
        this.dataConversion = dataConversion;
    }

    private String getSubstring(String dataStr, String startChar, String endChar){
        int startIndex = dataStr.indexOf(startChar) + startChar.length();
        int endIndex = dataStr.indexOf(endChar) ;
        return dataStr.substring(startIndex, endIndex);
    }


}
