package com.camunda.savingsaccount.process.activity.servicetask;

import com.camunda.savingsaccount.config.annotation.Profiling;
import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.process.model.InputData;
import com.camunda.savingsaccount.utils.Utils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Slf4j
@Profiling
@Component
@Builder
public class DetailsProduct implements JavaDelegate, Serializable {

    @Value("${application.product-controller-api.get-details-product.url}")
    private String getDetailsProductUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        InputData inputData = (InputData) delegateExecution.getVariable("inputData");
        if (inputData != null) {
            ResponseEntity<Product> productResponseEntity = restTemplate.exchange(
                    getDetailsProductUrl,
                    HttpMethod.GET,
                    new HttpEntity(inputData, Utils.getHttpHeaders()),
                    Product.class);
            Product resultProduct = productResponseEntity.getBody();
            delegateExecution.setVariable("detailsProduct", resultProduct);
            delegateExecution.setVariable("typeProduct", resultProduct.getType());

            delegateExecution.setVariable("statusReceiptDetailsProductErrorCode", Utils.SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusReceiptDetailsProductErrorMessage", Utils.SUCCESS_ERROR_DESCRIPTION);
            log.info("detailsProduct", productResponseEntity.getBody());
        } else {
            delegateExecution.setVariable("statusReceiptDetailsProductErrorCode", Utils.UN_SUCCESS_ERROR_CODE);
            delegateExecution.setVariable("statusReceiptDetailsProductErrorMessage", Utils.UN_SUCCESS_ERROR_DESCRIPTION);
            log.error("Execution broken, errors found from details product");
        }
    }

    public void setDetailsProductUrl(String getDetailsProductUrl) {
        this.getDetailsProductUrl = getDetailsProductUrl;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
