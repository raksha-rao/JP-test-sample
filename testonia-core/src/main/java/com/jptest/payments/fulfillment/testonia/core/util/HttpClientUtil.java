package com.jptest.payments.fulfillment.testonia.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Singleton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

@Singleton
public class HttpClientUtil {

    public String readContent(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        try (BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()))) {
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }

        return result.toString();
    }
}
