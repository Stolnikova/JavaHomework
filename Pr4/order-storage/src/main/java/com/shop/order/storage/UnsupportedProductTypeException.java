package com.shop.order.storage;

import lombok.Getter;

@Getter
public class UnsupportedProductTypeException extends RuntimeException {
    private final Class<?> productType;

    public UnsupportedProductTypeException(Class<?> productType) {
        super("Немає сховища для товарів типу " + nameOf(productType));
        this.productType = productType;
    }

    private static String nameOf(Class<?> type) {
        String simple = type.getSimpleName();
        return simple.isEmpty() ? type.getName() : simple;
    }
}
