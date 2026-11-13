package com.shop.order.app;

import com.github.javafaker.Faker;
import com.shop.order.model.Clothing;
import com.shop.order.model.Electronics;
import com.shop.order.model.Order;
import com.shop.order.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class OrderGenerator {
    private final Faker faker = new Faker(new Locale("uk"));
    private final AtomicInteger orderCounter = new AtomicInteger();

    Electronics electronics() {
        return Electronics.builder()
                .id(faker.code().ean8())
                .name(faker.options().option("Ноутбук", "Смартфон", "Навушники", "Планшет", "Монітор", "Смарт-годинник")
                        + " " + faker.bothify("??-###").toUpperCase())
                .brand(faker.options().option("Samsung", "Apple", "Lenovo", "Xiaomi", "Sony", "Asus"))
                .price(faker.number().randomDouble(2, 1500, 60000))
                .warrantyMonths(faker.options().option(6, 12, 24, 36))
                .build();
    }

    Clothing clothing() {
        return Clothing.builder()
                .id(faker.code().ean8())
                .name(faker.options().option("Футболка", "Куртка", "Светр", "Джинси", "Сукня", "Худі"))
                .size(faker.options().option("XS", "S", "M", "L", "XL"))
                .material(faker.options().option("бавовна", "льон", "вовна", "поліестер", "денім"))
                .color(faker.options().option("чорний", "білий", "синій", "зелений", "бежевий", "червоний"))
                .price(faker.number().randomDouble(2, 300, 5000))
                .build();
    }

    <T extends Product> Order<T> order(T product) {
        return order(product, faker.number().numberBetween(1, 5));
    }

    <T extends Product> Order<T> order(T product, int quantity) {
        return Order.<T>builder()
                .id(String.format("ORD-%03d", orderCounter.incrementAndGet()))
                .customer(faker.name().fullName())
                .product(product)
                .quantity(quantity)
                .build();
    }

    <T extends Product> List<Order<T>> orders(int count, Supplier<T> productSupplier) {
        return Stream.generate(productSupplier)
                .limit(count)
                .map(this::order)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
