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

import java.io.BufferedReader;
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
	private String json;

	public PIMResponse() {
	}

	public PIMResponse(int code, String message, String json) {
		this.code = code;
		this.message = message;
		this.json = json;
	}

	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getJson() {
		return json;
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
			String responseStr = responseString(response);
			if (status == 204) {
				return new PIMResponse(status, HttpUtil.getHeaderValue(response, "Location"), responseStr);
			}
			if (status >= 400) {
				try {
					JsonObject jsonObject = gson.fromJson(new InputStreamReader(response.getEntity().getContent()),
							JsonObject.class);
					if (jsonObject != null) {
						return new PIMResponse(jsonObject.get("code").getAsInt(),
								jsonObject.get("message").getAsString(), responseStr);
					}
				} catch (JsonSyntaxException e) {
					return new PIMResponse(status, e.getMessage(), responseStr);
				}
			}
			return new PIMResponse(status, null, responseStr);
		}
		
		private String responseString(HttpResponse response) {
			StringBuffer result = new StringBuffer();
			if (response.getEntity() != null) { 
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					while (bufferedReader.ready()) {
						result.append(bufferedReader.readLine());
					}
					System.out.println("Response: " + result.toString());
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result.toString();
		}
	}

}
