package com.camunda.savingsaccount.api.service;

import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.entity.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Profiling
@Service
public class ProductService {

    public Product getProduct(String cardNumber) {
        Product product;
        if (cardNumber != null && cardNumber.isEmpty() == false) {
            /*various operations to obtain product details,
            data could be sampled from different databases*/

            product = Product.builder()
                    .productId(UUID.randomUUID())
                    .name("nameProducts")
                    .type(ProductType.PT_1010)
                    .build();
            log.info("getProduct()",product);
            return product;
        }

        product = Product.builder()
                .productId(null)
                .name("-")
                .type(ProductType.PT_DEFAULT)
                .build();
        log.error("getProduct()",product);
        return product;
    }


}
