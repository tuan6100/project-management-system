package com.project.oop.PMS.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class RestTemplateConfig {

    @Value("${server.ssl.trust-store}")
    private String trustStorePath;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;


    @Bean
    public RestTemplate restTemplate() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream trustStoreStream = new FileInputStream(trustStorePath)) {
            trustStore.load(trustStoreStream, trustStorePassword.toCharArray());
        }
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(trustStore, null)
                .build();
        HttpClient httpClient = (HttpClient) HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

}
