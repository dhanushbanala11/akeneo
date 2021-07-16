package com.striketru.bc2akeneo.api;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.common.PIMResponse;
import com.striketru.bc2akeneo.util.HttpUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author anepolean
 */
public class ProductAPI {
    private static final Logger PRODUCTLOGGER = LogManager.getLogger(ProductAPI.class);
    /**
     * Variable for the pim client.
     */
    private final PIMClientAPI pimClient;

    /**
     * Constructor of ProductAPI.
     */
    public ProductAPI(Map<String, String> appProperties) {
        pimClient = HttpUtil.createPIMClient(appProperties);
    }

    
    public String upsertProductBySku(final String sku, final String content) throws IOException {
        String output = "In-progress...";
        PIMResponse response = pimClient.products.patch(sku, content);        
        if(response.getCode() == 204){
            output = "Update was successful";
        }else if(response.getCode() == 201){
            output = "Creation was successful";
        }
        PRODUCTLOGGER.debug("UpdateBySku :::"+output);
        return output;
    }

    public String upsertMutipleProducts(final String content) throws IOException {
        String output = "In-progress...";
        PIMResponse response = pimClient.multiproducts.patch(content);        
        if(response.getCode() == 204){
            output = "Update was successful";
        }else if(response.getCode() == 201){
            output = "Creation was successful";
        } else {
        	output = response.getJson();
        }
        PRODUCTLOGGER.debug("UpdateBySku :::"+output);
        return output;
    }

    public String createMediafile(final String widenurl, String jsonstring) throws IOException{
        PRODUCTLOGGER.debug("ProductBySku Mediafile:::"+widenurl);
    	String response = pimClient.productmedia.post(jsonstring, new File(widenurl));
    	PRODUCTLOGGER.debug("createMediafile :::"+response);
        return response;
    }
    
    /**
     * Returns the instance the product API.
     *
     * @return a product API.
     */

//    public String createUpdateProductBySku(final Map<String, Object> data, String sku) throws IOException, Exception {
//        String output = "In-progress...";
//        String reqObject = RequestUtil.request(data);
//        PIMResponse response = pimClient.products.patch(sku, reqObject);
//
//        if(response.getCode() == 204){
//            output = "204-Update was successful";
//        } else if(response.getCode() == 201){
//            output = "201-Creation was successful";
//        } else {
//        	output = "\n "+data.get("sku").toString()+" - "+response.getMessage()+" :: data: "+ data;
////            output = "Code: {} and Message: {}".format(String.valueOf(response.getCode()), customMessage);
//        }
//        //PRODUCTLOGGER.debug("UpdateBySku ::: "+output);
////        if (response.getCode() > 399){
////            throw new Exception();
////        }
//        return output;
//    }

}
