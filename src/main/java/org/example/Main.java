package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    static String urlAdress = "https://api.nasa.gov/planetary/apod?api_key=zOS7iZ2u6amQzRDnp5GIh7xYyvsvuE4PmiZTMHfi";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(urlAdress);

        CloseableHttpResponse response = httpClient.execute(request);

        JsonObject posts = mapper.readValue(
                response.getEntity().getContent(), new
                        TypeReference<>() {
                        });

        System.out.println(posts);

        String url = posts.getUrl();
        String nameApp = url.substring(url.lastIndexOf("/")+1);

        HttpGet request1 = new HttpGet(url);
        CloseableHttpResponse response1 = httpClient.execute(request1);

        byte[] arrByteFile = response1.getEntity().getContent().readAllBytes();

        File file = new File(nameApp);

        try (FileOutputStream out = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            bos.write(arrByteFile, 0, arrByteFile.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}