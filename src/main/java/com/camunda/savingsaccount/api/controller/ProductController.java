package com.camunda.savingsaccount.api.controller;

import com.camunda.savingsaccount.api.service.ProductService;
import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.process.model.InputData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profiling
@Component
@RestController
@RequestMapping("product-controller-api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/get-details-product", method = RequestMethod.GET)
    public Product getDataProductByCardId(@RequestBody InputData inputData) {
        if(inputData!=null){
            Product products = productService.getProduct(inputData.getCardNumber());
            log.info("getDataProductByCardId()",products);
            return products;
        }
        log.info("getDataProductByCardId() / null");
        return null;
    }


}
