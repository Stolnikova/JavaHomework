package com.shop.order.processing;

import com.shop.order.model.Order;
import com.shop.order.model.OrderStatus;
import com.shop.order.model.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class OrderFilters {

    public static <T extends Product> List<Order<T>> ofType(
            Collection<? extends Order<? extends Product>> orders, Class<T> type) {
        return orders.stream()
                .filter(order -> order.contains(type))
                .map(order -> order.as(type))
                .collect(Collectors.toList());
    }

    public static <T extends Product> List<Order<T>> where(
            Collection<Order<T>> orders, Predicate<? super Order<T>> condition) {
        return orders.stream().filter(condition).collect(Collectors.toList());
    }

    public static <T extends Product> Optional<Order<T>> mostExpensive(Collection<Order<T>> orders) {
        return orders.stream().max(Comparator.comparingDouble(Order::getTotal));
    }

    public static double revenue(Collection<? extends Order<? extends Product>> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(Order::getPaidTotal)
                .sum();
    }
}
