package com.camunda.savingsaccount.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ProductType implements Serializable {
    PT_1010(1L),
    PT_1020(2L),
    PT_1030(3L),
    PT_DEFAULT(4L);

    private final Long id;
    private static final Map<Long, ProductType> VALUES_BY_ID = (Map) Stream.of(values()).collect(Collectors.collectingAndThen(Collectors.toMap(ProductType::getId, Function.identity()), Collections::unmodifiableMap));

    public static ProductType byId(Long id) {
        return Optional.ofNullable((ProductType)VALUES_BY_ID.get(id)).orElseThrow(() -> {
            return new IllegalArgumentException(String.format("Product type for %d is not presented!", id));
        });
    }

    private ProductType(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
}
