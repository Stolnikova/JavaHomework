package com.shop.order.model;

import lombok.Getter;

@Getter
public class ProductTypeMismatchException extends RuntimeException {
    private final Class<?> expectedType;
    private final Class<?> actualType;

    public ProductTypeMismatchException(Class<?> expectedType, Class<?> actualType) {
        super("Очікувався товар типу " + nameOf(expectedType) + ", а отримано " + nameOf(actualType));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    private static String nameOf(Class<?> type) {
        String simple = type.getSimpleName();
        return simple.isEmpty() ? type.getName() : simple;
    }
}
