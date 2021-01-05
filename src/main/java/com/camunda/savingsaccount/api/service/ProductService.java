package com.camunda.savingsaccount.api.service;

import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.entity.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Profiling
@Service
public class ProductService {

    public Product getProduct(String cardNumber) {
            Product product;
            /*various operations to obtain product details,
            data could be sampled from different databases*/
            String codeCardNumber = Arrays.asList(cardNumber.split("-")).get(0);
            if(codeCardNumber.contains("4444")){
                product = Product.builder()
                        .productId(UUID.randomUUID())
                        .name("product-1")
                        .type(ProductType.PT_1010)
                        .build();
            }else if(codeCardNumber.contains("4445")){
                product = Product.builder()
                        .productId(UUID.randomUUID())
                        .name("product-2")
                        .type(ProductType.PT_1020)
                        .build();
            }else if(codeCardNumber.contains("4446")){
                product = Product.builder()
                        .productId(UUID.randomUUID())
                        .name("product-3")
                        .type(ProductType.PT_1030)
                        .build();
            }else{
                product = Product.builder()
                        .productId(null)
                        .name("-")
                        .type(ProductType.PT_DEFAULT)
                        .build();
                log.error("getProduct()",product);
                return product;
            }

            log.info("getProduct()",product);
            return product;
    }


}
