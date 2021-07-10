/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.striketru.bc2akeneo.util.HttpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;

/**
 *
 * @author anepolean
 */
public class PIMResponse {
    private static final Gson gson = new Gson();
    private int code;
    private String message;

    public PIMResponse() {
    }

    public PIMResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return code < 300;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public PIMResponse from(HttpResponse response) throws IOException {
            int status = response.getStatusLine().getStatusCode();
            if(status == 204) {
                return new PIMResponse(status, HttpUtil.getHeaderValue(response,"Location"));                    
            }
            if (status >= 400) {
                try {
                    JsonObject jsonObject = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), JsonObject.class);
                    if (jsonObject != null) {
                        return new PIMResponse(jsonObject.get("code").getAsInt(), jsonObject.get("message").getAsString());
                    }
                } catch (JsonSyntaxException e) {
                    return new PIMResponse(status, e.getMessage());
                }
            }
            return new PIMResponse(status, null);
        }
    }    
}
