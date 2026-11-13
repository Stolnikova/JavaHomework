package com.shop.order.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Product {
    private final String id;
    private final String name;
    private final double price;

    protected Product(String id, String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною: " + price);
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public abstract String getCategory();
}
