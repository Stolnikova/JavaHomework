package com.shop.order.threads.internal;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class NamedThreadFactory implements ThreadFactory {
    private final String prefix;
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(Runnable task) {
        Thread thread = new Thread(task, prefix + "-" + counter.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }
}
