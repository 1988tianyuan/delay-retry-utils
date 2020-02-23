package com.liugeng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Slf4j
public class DelayTask implements Delayed {

    private volatile Instant expire;
    private Callable<String> task;

    public DelayTask(Instant expire, Callable<String> task) {
        this.expire = expire;
        this.task = task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long expireNano = expire.toEpochMilli();
        long nowNano = Instant.now().toEpochMilli();
        long delay = unit.convert(expireNano - nowNano, TimeUnit.MILLISECONDS);
        return delay;
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

    public void setExpire(Instant expire) {
        this.expire = expire;
    }

    public Instant incrExpire(long incr, ChronoUnit timeUnit) {
        Instant now = Instant.now();
        expire = now.plus(incr, timeUnit);
        return expire;
    }

    public Instant getExpire() {
        return expire;
    }

    public CompletableFuture<String> asyncDoTask(Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
