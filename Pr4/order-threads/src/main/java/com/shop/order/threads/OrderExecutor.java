package com.shop.order.threads;

import com.shop.order.model.Order;
import com.shop.order.model.Product;
import com.shop.order.threads.internal.NamedThreadFactory;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderExecutor implements AutoCloseable {
    private final ExecutorService pool;

    public OrderExecutor(int threads, String threadPrefix) {
        this.pool = Executors.newFixedThreadPool(threads, new NamedThreadFactory(threadPrefix));
    }

    public <T extends Product> ProcessingReport processAll(String title, List<Order<T>> orders, OrderTask<T> task) {
        AtomicInteger succeeded = new AtomicInteger();
        List<String> errors = new CopyOnWriteArrayList<>();
        Set<String> threadNames = ConcurrentHashMap.newKeySet();
        long start = System.nanoTime();

        List<Callable<Void>> jobs = orders.stream()
                .map(order -> (Callable<Void>) () -> {
                    threadNames.add(Thread.currentThread().getName());
                    try {
                        task.handle(order);
                        succeeded.incrementAndGet();
                    } catch (Exception e) {
                        errors.add(e.getMessage());
                    }
                    return null;
                })
                .collect(Collectors.toList());

        try {
            pool.invokeAll(jobs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            errors.add("Обробку перервано");
        }

        return ProcessingReport.builder()
                .title(title)
                .total(orders.size())
                .succeeded(succeeded.get())
                .errors(errors)
                .threadNames(new TreeSet<>(threadNames))
                .durationMillis(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start))
                .build();
    }

    @Override
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
