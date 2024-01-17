package com.xpressvtu.xpressvtu.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;

@Configuration
public class RestConfig {
//    @Value("${base.url}")
//    private String baseUrl;
//
//    @Bean
//    RestClient restClient(){
//        return RestClient.create(baseUrl);
//    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
}
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
    @Bean
     HttpClient createHttpClientBean(){
        return HttpClient.newHttpClient();
    }
}
