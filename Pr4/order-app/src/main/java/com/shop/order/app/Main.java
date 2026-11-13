package com.shop.order.app;

import com.shop.order.model.Clothing;
import com.shop.order.model.Electronics;
import com.shop.order.model.Order;
import com.shop.order.model.Product;
import com.shop.order.model.ProductTypeMismatchException;
import com.shop.order.processing.OrderFilters;
import com.shop.order.processing.OrderProcessor;
import com.shop.order.storage.OrderRepository;
import com.shop.order.storage.OrderStorage;
import com.shop.order.storage.UnsupportedProductTypeException;
import com.shop.order.threads.OrderExecutor;
import com.shop.order.threads.ProcessingReport;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        OrderGenerator generator = new OrderGenerator();

        List<Order<Electronics>> electronicsOrders = generator.orders(6, generator::electronics);
        List<Order<Clothing>> clothingOrders = generator.orders(6, generator::clothing);
        clothingOrders.add(generator.order(generator.clothing(), 0));

        List<Order<? extends Product>> allOrders = new ArrayList<>();
        allOrders.addAll(electronicsOrders);
        allOrders.addAll(clothingOrders);

        printHeader("Нові замовлення");
        allOrders.forEach(OrderProcessor::processOrder);

        OrderStorage storage = new OrderStorage();
        OrderRepository<Electronics> electronicsRepository = storage.register(Electronics.class);
        OrderRepository<Clothing> clothingRepository = storage.register(Clothing.class);

        OrderProcessor<Electronics> electronicsProcessor = new OrderProcessor<>(electronicsRepository::save);
        OrderProcessor<Clothing> clothingProcessor = new OrderProcessor<>(clothingRepository::save);

        printHeader("Фільтрація за типом");
        List<Order<Electronics>> filteredElectronics = OrderFilters.ofType(allOrders, Electronics.class);
        List<Order<Clothing>> filteredClothing = OrderFilters.ofType(allOrders, Clothing.class);
        System.out.println("Електроніка: " + filteredElectronics.size() + ", одяг: " + filteredClothing.size());

        printHeader("Багатопотокова обробка");
        List<ProcessingReport> reports = new ArrayList<>();
        try (OrderExecutor executor = new OrderExecutor(3, "order-worker")) {
            reports.add(executor.processAll("Електроніка", filteredElectronics, electronicsProcessor::process));
            reports.add(executor.processAll("Одяг", filteredClothing, clothingProcessor::process));
        }

        Runnable reportTask = () -> {
            printHeader("Звіт (потік " + Thread.currentThread().getName() + ")");
            reports.stream().map(ProcessingReport::summary).forEach(System.out::println);
            reports.stream().flatMap(report -> report.getErrors().stream())
                    .forEach(error -> System.out.println("  Помилка: " + error));
        };
        Thread reportThread = new Thread(reportTask, "report-thread");
        reportThread.start();
        reportThread.join();

        printHeader("Сховище");
        System.out.println("Збережено замовлень: " + storage.statistics());
        OrderFilters.mostExpensive(electronicsRepository.findAll())
                .ifPresent(order -> System.out.println("Найдорожче замовлення електроніки: "
                        + order.getId() + " — " + order.getProduct().getName() + ", " + order.getProduct().getBrand()
                        + String.format(", %.2f грн", order.getTotal())));
        List<Order<Clothing>> bigClothing = clothingRepository.findBy(order -> order.getQuantity() >= 3);
        System.out.println("Замовлення одягу від 3 шт. (знижка 10%): " + bigClothing.size());
        bigClothing.forEach(order -> System.out.println("  " + order.getId() + " " + order.getProduct().getName()
                + " " + order.getProduct().getSize() + ", " + order.getProduct().getColor()
                + " x" + order.getQuantity()));
        System.out.printf("Загальний виторг: %.2f грн%n", OrderFilters.revenue(storage.findAll()));

        printHeader("Обробка виключень для узагальнених типів");
        try {
            electronicsRepository.saveChecked(clothingOrders.get(0));
        } catch (ProductTypeMismatchException e) {
            System.out.println("ProductTypeMismatchException: " + e.getMessage());
        }

        try {
            Order<Electronics> wrong = clothingOrders.get(1).as(Electronics.class);
            System.out.println("Не мало виконатися: " + wrong.getId());
        } catch (ProductTypeMismatchException e) {
            System.out.println("ProductTypeMismatchException: " + e.getMessage());
        }

        try {
            storage.store(generator.order(new GiftCard(500)));
        } catch (UnsupportedProductTypeException e) {
            System.out.println("UnsupportedProductTypeException: " + e.getMessage());
        }

        try {
            OrderRepository<GiftCard> giftCards = storage.of(GiftCard.class);
            System.out.println("Не мало виконатися: " + giftCards.size());
        } catch (UnsupportedProductTypeException e) {
            System.out.println("UnsupportedProductTypeException: " + e.getMessage());
        }
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private static final class GiftCard extends Product {
        private GiftCard(double amount) {
            super("GIFT-" + (int) amount, "Подарунковий сертифікат", amount);
        }

        @Override
        public String getCategory() {
            return "Сертифікати";
        }
    }
}
