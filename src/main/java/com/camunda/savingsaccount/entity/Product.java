package com.camunda.savingsaccount.entity;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Product {
    @NotNull
    private final UUID productId;
    @NotNull
    private final String name;
    @NotNull
    private BigDecimal sum;
    @NotNull
    private final ProductType type;

    /*TODO ... other fields and methods*/
}
