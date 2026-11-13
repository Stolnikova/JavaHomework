package com.shop.order.storage.internal;

import com.shop.order.model.Order;
import com.shop.order.model.Product;
import com.shop.order.storage.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InMemoryOrderRepository<T extends Product> implements OrderRepository<T> {
    @Getter
    private final Class<T> productType;
    private final Map<String, Order<T>> orders = new ConcurrentHashMap<>();

    @Override
    public void save(Order<T> order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Order<T> saveChecked(Order<? extends Product> order) {
        Order<T> typed = order.as(productType);
        save(typed);
        return typed;
    }

    @Override
    public Optional<Order<T>> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order<T>> findAll() {
        return findBy(order -> true);
    }

    @Override
    public List<Order<T>> findBy(Predicate<? super Order<T>> condition) {
        return orders.values().stream()
                .filter(condition)
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return orders.size();
    }
}
