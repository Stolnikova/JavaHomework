package com.shop.order.threads;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class ProcessingReport {
    String title;
    int total;
    int succeeded;
    @Singular
    List<String> errors;
    @Singular
    Set<String> threadNames;
    long durationMillis;

    public String summary() {
        return String.format("%s: оброблено %d з %d за %d мс, потоки %s",
                title, succeeded, total, durationMillis, threadNames);
    }
}
