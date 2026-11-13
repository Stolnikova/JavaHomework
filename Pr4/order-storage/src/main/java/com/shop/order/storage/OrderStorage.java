package com.shop.order.storage;

import com.shop.order.model.Order;
import com.shop.order.model.Product;
import com.shop.order.storage.internal.InMemoryOrderRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderStorage {
    private final Map<Class<? extends Product>, OrderRepository<? extends Product>> repositories =
            new ConcurrentHashMap<>();

    public <T extends Product> OrderRepository<T> register(Class<T> type) {
        repositories.computeIfAbsent(type, InMemoryOrderRepository::new);
        return of(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends Product> OrderRepository<T> of(Class<T> type) {
        OrderRepository<? extends Product> repository = repositories.get(type);
        if (repository == null) {
            throw new UnsupportedProductTypeException(type);
        }
        return (OrderRepository<T>) repository;
    }

    public void store(Order<? extends Product> order) {
        Class<? extends Product> actual = order.getProduct().getClass();
        repositories.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(actual))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new UnsupportedProductTypeException(actual))
                .saveChecked(order);
    }

    public List<Order<? extends Product>> findAll() {
        List<Order<? extends Product>> all = new ArrayList<>();
        repositories.values().forEach(repository -> all.addAll(repository.findAll()));
        return all;
    }

    public Map<String, Integer> statistics() {
        Map<String, Integer> result = new LinkedHashMap<>();
        repositories.forEach((type, repository) -> result.put(type.getSimpleName(), repository.size()));
        return result;
    }
}
