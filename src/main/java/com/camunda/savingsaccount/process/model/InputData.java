package com.camunda.savingsaccount.process.model;

import com.camunda.savingsaccount.entity.Product;
import com.camunda.savingsaccount.entity.ProductType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
public class InputData implements Serializable {
    private final String cardNumber;
    private final BigDecimal sum;
}
