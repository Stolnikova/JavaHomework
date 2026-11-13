package com.shop.order.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    NEW("Нове"),
    PROCESSING("В обробці"),
    COMPLETED("Виконано"),
    REJECTED("Відхилено");

    private final String title;
}
