package com.shop.order.storage;

import com.shop.order.model.Order;
import com.shop.order.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface OrderRepository<T extends Product> {

    Class<T> getProductType();

    void save(Order<T> order);

    Order<T> saveChecked(Order<? extends Product> order);

    Optional<Order<T>> findById(String id);

    List<Order<T>> findAll();

    List<Order<T>> findBy(Predicate<? super Order<T>> condition);

    int size();
}
