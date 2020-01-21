package com.microservice.apt.olv.service;

import feign.Client;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class OlvFeignConfiguration {

    @Value("${feign.olv-api.username:}")
    private String username;

    @Value("${feign.olv-api.password:}")
    private String password;

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

}
