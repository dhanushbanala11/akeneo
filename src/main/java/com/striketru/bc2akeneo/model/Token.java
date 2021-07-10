/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author anepolean
 */
public class Token {
    @SerializedName("access_token")
    private String accessToken;

    public Token() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }    
}
