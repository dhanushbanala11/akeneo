/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.util;

import com.striketru.bc2akeneo.api.PIMClientAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 *
 * @author anepolean
 */
public class HttpUtil {
    private static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }
    
    public static String getHeaderValue(HttpResponse httpResponse, String str) {
        if (httpResponse == null || str == null) {
            return null;
        }
        Header[] allHeaders = httpResponse.getAllHeaders();
        if (allHeaders == null || allHeaders.length <= 0) {
            return null;
        }
        for (Header header : allHeaders) {
            if (header != null) {
                String name = header.getName();
                if (name != null && name.equalsIgnoreCase(str)) {
                    return header.getValue();
                }
            }
        }
        return null;
    }
    
    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
    
    public static String StreamToResponse(InputStream is) throws IOException
    {
        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            String output;
            response = new StringBuilder();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
        }
        return response.toString();
    }
    
    public static PIMClientAPI createPIMClient(String url, String client, String secret, String username, String password) {
        PIMClientAPI pimClient = PIMClientAPI.builder()
        		.url(url).clientId(client).secret(secret).username(username).password(password)
                .build();
        pimClient.authenticate();
        return pimClient;
    }
    
    public static String removeQueryParameters(String url) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> urlParameters = uriBuilder.getQueryParams();
        URIBuilder builder = new URIBuilder();
        builder.setParameters(urlParameters);
        return builder.toString();
    }    
}
