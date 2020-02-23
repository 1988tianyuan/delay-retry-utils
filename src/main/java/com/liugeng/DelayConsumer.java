package com.liugeng;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;

@Slf4j
public class DelayConsumer {

    private DelayQueue<DelayTask> delayQueue;
    private Executor executor;
    private static final long fixDelaySec = 5;

    public DelayConsumer(DelayQueue<DelayTask> delayQueue, Executor executor) {
        this.delayQueue = delayQueue;
        this.executor = executor;
    }

    public void consume() {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                pollTask();
            }
        }).start();
    }

    private void pollTask() {
        final DelayTask task;
        try {
            task = delayQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        task.asyncDoTask(executor)
            .whenCompleteAsync((result, e) -> {
                if ("SUCCESS".equals(result)) {
                    log.info("本次任务成功！");
                } else {
                    Instant newExpire = task.incrExpire(fixDelaySec, ChronoUnit.SECONDS);
                    log.warn("本次任务失败，延迟到{}秒后。", LocalDateTime.ofInstant(newExpire, ZoneId.systemDefault()));
                    delayQueue.offer(task);
                }
            }, executor);
    }
}
