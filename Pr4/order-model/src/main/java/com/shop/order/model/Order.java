package com.shop.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Order<T extends Product> {
    private final String id;
    private final String customer;
    private final T product;
    private final int quantity;
    @Setter
    private volatile OrderStatus status;
    @Setter
    private volatile double paidTotal;

    @Builder
    public Order(String id, String customer, T product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Замовлення " + id + " не містить товару");
        }
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.status = OrderStatus.NEW;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    public boolean contains(Class<? extends Product> type) {
        return type.isInstance(product);
    }

    @SuppressWarnings("unchecked")
    public <U extends Product> Order<U> as(Class<U> type) {
        if (!type.isInstance(product)) {
            throw new ProductTypeMismatchException(type, product.getClass());
        }
        return (Order<U>) (Order<?>) this;
    }
}
