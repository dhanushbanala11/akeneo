/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.striketru.bc2akeneo.common.PIMResponse;
import com.striketru.bc2akeneo.model.Token;

public class PIMClientAPI {
    private final URI uri;
    private final String clientId;
    private final String secret;
    private final String username;
    private final String password;

    private String authToken;
    private String authHeader;

    public class PIMResource {
        private final String path;

        public PIMResource(String path) {
            this.path = path;
        }

        @SuppressWarnings("unused")
		private boolean isSuccess(HttpResponse response){
            return statusOf(response) == 204 || statusOf(response) == 200;
        }

        private boolean isPostSuccess(HttpResponse response)  {
            return statusOf(response) == 201;
        }

        private String getAsString(HttpEntity entity) throws IOException{
            StringWriter sw = new StringWriter();

            InputStream content = entity.getContent();
            int i = 0;
            while((i = content.read()) != -1) {
                sw.append((char)i);
            }
            return sw.toString();
        }


        public PIMResponse patch(String resourceId, String content) throws IOException {

            URI resourceUri = uri;
            if(StringUtils.isNotEmpty(resourceId)){
                resourceUri = uri.resolve(path.replace("{code}", urlencode(resourceId)));
            }
            HttpResponse response = patch(resourceUri, content, true);
            return PIMResponse.builder().from(response);
        }
        
        public PIMResponse patch(String content) throws IOException {

            URI resourceUri = uri.resolve(path);
            ContentType vnd_akeneo_collection_content_type =  ContentType.create("application/vnd.akeneo.collection+json", "UTF-8");
            HttpResponse response = patch(resourceUri, content, true, vnd_akeneo_collection_content_type);
            return PIMResponse.builder().from(response);
        }
        	
        private HttpResponse patch(URI resourceUri, String content, boolean retry, ContentType contentType) throws IOException {
            Request request = Request.Patch(resourceUri)
                    .addHeader("Authorization", authHeader)
                    .bodyString(content, contentType);
            Response response = request.execute();
            HttpResponse patchResponse = response.returnResponse();
            if (isUnauthenticated(patchResponse) && retry) {
                authenticate();
                return patch(resourceUri, content, false);
            }
            return patchResponse;
        }
        
//        private HttpResponse get(URI resourceUri, boolean retry) throws IOException {
//            Request request = Request.Get(resourceUri).addHeader("Authorization", authHeader);
//            HttpResponse response = request.execute().returnResponse();
//            if (isUnauthenticated(response) && retry) {
//                authenticate();
//                return get(resourceUri, false);
//            }
//            return response;
//        }
        private HttpResponse patch(URI resourceUri, String content, boolean retry) throws IOException {
        	return patch(resourceUri, content, retry, ContentType.APPLICATION_JSON);
        }
        
        public String post(String jsonstring, File file) throws IOException {
            URI resourceUri = uri.resolve(path);
            HttpResponse response = post(resourceUri, jsonstring, file, true);
            System.out.println(response);
            if(isPostSuccess(response)) {
                return getAsString(response.getEntity());
            } else {
                int statusOf = statusOf(response);
                return "FAILED call, responded: " + statusOf;
            }
        }
        
        private HttpResponse post(URI resourceUri, String postData, File fileUpload, boolean retry) throws IOException {
            System.out.println(authHeader);
            System.out.println(fileUpload.getName());
            System.out.println(postData);
            HttpClient client = HttpClientBuilder.create().build();            
            HttpPost post = new HttpPost(resourceUri.normalize());
            post.addHeader("Authorization", authHeader);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", fileUpload, ContentType.DEFAULT_BINARY, fileUpload.getName());
            if(postData.contains("code")) {
            	builder.addTextBody("product_model", postData, ContentType.TEXT_PLAIN);
            }else {
            	builder.addTextBody("product", postData, ContentType.TEXT_PLAIN);
            }
            
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (isUnauthenticated(response) && retry) {
                authenticate();
                System.out.println("Authne"+authenticate());
                return post(resourceUri, postData, fileUpload, false);
            }

            return response;
        }

        private boolean isUnauthenticated(HttpResponse response) throws IOException {
            return statusOf(response) == 401;
        }

        private int statusOf(HttpResponse response) {
            return response.getStatusLine().getStatusCode();
        }        
    }
    public final PIMResource products = new PIMResource("/api/rest/v1/products/{code}");
    public final PIMResource multiproducts = new PIMResource("/api/rest/v1/products");
    public final PIMResource searchproducts = new PIMResource("/api/rest/v1/products?search={query}");
    public final PIMResource paginateproducts = new PIMResource("/api/rest/v1/products{query}");
    public final PIMResource productmedia = new PIMResource("/api/rest/v1/media-files");

    private static String urlencode(String value) {
        return value != null ? value.replace(" ", "%20").replace("#", "%23") : value;
    }

    public PIMClientAPI(URI uri, String clientId, String secret, String username, String password) {
        this.uri = uri;
        this.clientId = clientId;
        this.secret = secret;
        this.username = username;
        this.password = password;
    }
    
    public boolean authenticate() {
        Optional<Token> tokenResponse = getAuthToken();
        if (tokenResponse.isPresent()) {
            authToken = tokenResponse.get().getAccessToken();
            authHeader = "Bearer " + authToken;
            return true;
        }
        return false;
    }
    
    private Optional<Token> getAuthToken() {
        try {
            String requestHeader = getAuthHeader(clientId, secret);
            String requestBody = getAuthBody(username, password);
            Response response = Request.Post(uri.resolve("/api/oauth/v1/token"))
                    .addHeader("Authorization", requestHeader)
                    .bodyString(requestBody, ContentType.APPLICATION_JSON)
                    .execute();
            Gson gson = new Gson();
            String content = response.returnContent().asString();
            Token tokenResponse = gson.fromJson(content, Token.class);
            return Optional.of(tokenResponse);
        } catch (IOException e) {
            // ignore
        }

        return Optional.empty();
    }
    
    private String getAuthHeader(String clientId, String secret) {
        String toEncode = String.join(":", clientId, secret);
        return "Basic " + Base64.getEncoder().encodeToString(toEncode.getBytes());
    }
    
    private String getAuthBody(String username, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", username);
        requestBody.addProperty("password", password);
        requestBody.addProperty("grant_type", "password");
        return requestBody.toString();
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {

        private String url;
        private String clientId;
        private String secret;
        private String username;
        private String password;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public PIMClientAPI build() {
            URI uri = URI.create(url);
            return new PIMClientAPI(uri, clientId, secret, username, password);
        }
    }    
}
