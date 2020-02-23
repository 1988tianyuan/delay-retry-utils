package com.liugeng.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
public class DelayTask<T> implements Delayed {

    private volatile Instant expire;
    private final Callable<T> task;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    public DelayTask(Instant expire, Callable<T> task) {
        this.expire = expire;
        this.task = task;
    }

    public Instant newRetry(long incr, ChronoUnit timeUnit) {
        Instant now = Instant.now();
        expire = now.plus(incr, timeUnit);
        retryCount.incrementAndGet();
        return expire;
    }

    public int getRetryCount() {
        return retryCount.get();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long expireNano = expire.toEpochMilli();
        long nowNano = Instant.now().toEpochMilli();
        return unit.convert(expireNano - nowNano, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long curDelayNano = expire.getNano();
        long cpDelayNano = o.getDelay(TimeUnit.NANOSECONDS);
        long gap = curDelayNano - cpDelayNano;
        if (gap == 0) {
            return 0;
        } else {
            return gap > 0 ? 1 : -1;
        }
    }

    public T doTask() throws Exception {
        return task.call();
    }

    public CompletableFuture<T> asyncDoTask(Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
