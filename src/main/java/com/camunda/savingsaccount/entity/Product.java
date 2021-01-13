package com.camunda.savingsaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Product implements Serializable {
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
