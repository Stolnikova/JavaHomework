package com.shop.order.threads;

import com.shop.order.model.Order;
import com.shop.order.model.Product;

@FunctionalInterface
public interface OrderTask<T extends Product> {
    void handle(Order<T> order) throws Exception;
}
