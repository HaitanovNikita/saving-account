package com.camunda.savingsaccount.config;

import com.camunda.savingsaccount.api.service.ProductService;
import com.camunda.savingsaccount.api.service.SavingsAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public ProductService productService(){
        return new ProductService();
    }

    @Bean
    public SavingsAccountService savingsAccountService(){
        return new SavingsAccountService();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders(){
        return new HttpHeaders();
    }


}
