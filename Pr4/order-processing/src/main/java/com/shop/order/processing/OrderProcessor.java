package com.shop.order.processing;

import com.shop.order.model.Order;
import com.shop.order.model.OrderStatus;
import com.shop.order.model.Product;
import com.shop.order.processing.internal.PriceCalculator;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class OrderProcessor<T extends Product> {
    private final Consumer<? super Order<T>> onCompleted;

    public OrderProcessor(Consumer<? super Order<T>> onCompleted) {
        this.onCompleted = onCompleted;
    }

    public Order<T> process(Order<T> order) throws OrderProcessingException {
        order.setStatus(OrderStatus.PROCESSING);
        try {
            validate(order);
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
        } catch (OrderProcessingException e) {
            order.setStatus(OrderStatus.REJECTED);
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            order.setStatus(OrderStatus.REJECTED);
            throw new OrderProcessingException("Обробку замовлення " + order.getId() + " перервано", e);
        }

        order.setPaidTotal(PriceCalculator.finalTotal(order));
        order.setStatus(OrderStatus.COMPLETED);
        onCompleted.accept(order);
        System.out.printf("[%s] %s: %s x%d — %.2f грн (знижка %.0f%%) — %s%n",
                Thread.currentThread().getName(), order.getId(), order.getProduct().getName(),
                order.getQuantity(), order.getPaidTotal(), PriceCalculator.discountRate(order) * 100,
                order.getStatus().getTitle());
        return order;
    }

    public static void processOrder(Order<? extends Product> order) {
        Product product = order.getProduct();
        System.out.printf("Прийнято замовлення %s: [%s] %s, %d шт. — %s%n",
                order.getId(), product.getCategory(), product.getName(), order.getQuantity(), order.getCustomer());
    }

    private void validate(Order<T> order) throws OrderProcessingException {
        if (order.getQuantity() <= 0) {
            throw new OrderProcessingException("Замовлення " + order.getId()
                    + ": кількість має бути більшою за нуль, а вказано " + order.getQuantity());
        }
        if (order.getProduct().getPrice() == 0) {
            throw new OrderProcessingException("Замовлення " + order.getId() + ": товар без ціни");
        }
        if (order.getCustomer() == null || order.getCustomer().isBlank()) {
            throw new OrderProcessingException("Замовлення " + order.getId() + ": не вказано покупця");
        }
    }
}
