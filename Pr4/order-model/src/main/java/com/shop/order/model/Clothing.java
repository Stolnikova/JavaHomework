package com.shop.order.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Clothing extends Product {
    private final String size;
    private final String material;
    private final String color;

    @Builder
    public Clothing(String id, String name, double price, String size, String material, String color) {
        super(id, name, price);
        this.size = size;
        this.material = material;
        this.color = color;
    }

    @Override
    public String getCategory() {
        return "Одяг";
    }
}
