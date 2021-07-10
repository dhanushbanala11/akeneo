/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author anepolean
 */
public class DataUtil {
    private static final ObjectMapper OM = new ObjectMapper();

    public static Map jsonStringToMap(String jsonString) throws IOException {
        return OM.readValue(jsonString, Map.class);
    }
    
    public static String[] concatArrays(final String[]... arrays) {
        return Arrays.stream(arrays)
         .flatMap(Arrays::stream)
         .toArray(String[]::new);
    }
    
    public static String removeLeadZeros(String str){
        StringBuilder sb = new StringBuilder(str);
        while (sb.length()>1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        return sb.toString();  // return in String
    }
    
    public static String getWithoutExtension(String filename, boolean ispdf){
        if(filename.lastIndexOf('.') < 0){
            return filename;
        }
        String withoutextension = filename.substring(0, filename.lastIndexOf('.'));
        if(ispdf){
            return withoutextension;
        }
        if(filename.indexOf('_') > 0) {
            withoutextension = withoutextension.substring(0, withoutextension.indexOf('_'));
        }
        return withoutextension;
    }
    
    public static String addToString(String str, int pos, String ins) {
        String singleskuadded = str.substring(0, pos) + ins + str.substring(pos);
        return singleskuadded.replaceAll("'","\"");
    }
    
    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String getTheNewestFile(String filePath, String ext) {
        File theNewestFile = null;
        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("*." + ext);
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {
            /** The newest file comes first **/
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
        }
        if(theNewestFile != null)
            return theNewestFile.getName();
        
        return "";
    }
    

   /**
       * converts time (in milliseconds) to human-readable format
       *  "<dd:>hh:mm:ss"
     * @param duration
    */
    public static String millisToShortDHMS(long duration) {
        String res = "";
        long days  = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                       - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                         - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                       - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0) {
          res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        else {
          res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return res;
    }
  
    public static String removeZero(String str) 
    { 
        if("00".equals(str))
            return "1";
        // Count leading zeros 
        int i = 0; 
        while (i < str.length() && str.charAt(i) == '0') 
            i++; 
  
        // Convert str into StringBuffer as Strings 
        // are immutable. 
        StringBuilder sb = new StringBuilder(str); 
  
        // The  StringBuffer replace function removes 
        // i characters from given index (0 here) 
        sb.replace(0, i, ""); 
  
        return sb.toString();  // return in String 
    } 
    
    /**It generates akeneo code for the given input text.
     * @param inputText
     * @return
     */
    public static String generateAkenoCode(String inputText) {
        inputText = inputText.trim();
        inputText = inputText.replaceAll("&#174;", ""); // "TEMPUR&#174;" is
                                                        // the only instance
        inputText = inputText.replaceAll("[^\\p{ASCII}]", "");// ASCII characters

        // so far
        inputText = inputText.replaceAll("\\?", "_");
        inputText = inputText.replaceAll("!", "_");
        inputText = inputText.replaceAll("\\*", "_");
        inputText = inputText.replaceAll("%", "_");
        inputText = inputText.replaceAll("#", "_");
        inputText = inputText.replaceAll("-", "_");
        inputText = inputText.replaceAll("/", "_");
        inputText = inputText.replaceAll("\\.", "_");
        inputText = inputText.replaceAll("'", "_");
        inputText = inputText.replaceAll("\\\"", "_");
        inputText = inputText.replaceAll("\\[", "_");
        inputText = inputText.replaceAll("\\]", "_");
        inputText = inputText.replaceAll(" ", "_");
        inputText = inputText.replaceAll(">", "_");
        inputText = inputText.replaceAll("<", "_");
        inputText = inputText.replaceAll("\\(", "");
        inputText = inputText.replaceAll("\\)", "");
        inputText = inputText.replaceAll("&", "");
        inputText = inputText.replaceAll(",", "_");
        inputText = inputText.replaceAll(";", "_");
        inputText = inputText.replaceAll("@", "_");
        inputText = inputText.replaceAll(":", "_");
        inputText = inputText.replaceAll("\\+", "_");
        inputText = inputText.replaceAll("\\_+", "_");
        if (inputText.endsWith("_")) {
            inputText = inputText.substring(0, (inputText.length() - 1));
        }
        inputText = inputText.toLowerCase();

        if (inputText.length() > 100) {
            return inputText.substring(0, 100);
        }

        return inputText;
    }    
}
