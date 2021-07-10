package com.striketru.bc2akeneo.api;

import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.striketru.bc2akeneo.common.PIMResponse;
import com.striketru.bc2akeneo.util.HttpUtil;
import com.striketru.bc2akeneo.util.RequestUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /**
     * Returns the instance the product API.
     *
     * @return a product API.
     */

    public String createUpdateProductBySku(final Map<String, Object> data, String sku) throws IOException, Exception {
        String output = "In-progress...";
        String reqObject = RequestUtil.request(data);
        PIMResponse response = pimClient.products.patch(sku, reqObject);

        if(response.getCode() == 204){
            output = "204-Update was successful";
        } else if(response.getCode() == 201){
            output = "201-Creation was successful";
        } else {
        	output = "\n "+data.get("sku").toString()+" - "+response.getMessage()+" :: data: "+ data;
//            output = "Code: {} and Message: {}".format(String.valueOf(response.getCode()), customMessage);
        }
        //PRODUCTLOGGER.debug("UpdateBySku ::: "+output);
//        if (response.getCode() > 399){
//            throw new Exception();
//        }
        return output;
    }

}
