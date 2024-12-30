package com.project.oop.PMS.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.config.Lookup;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        DefaultClientTlsStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext);
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager((Lookup<ConnectionSocketFactory>) tlsStrategy);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
