package com.liugeng.retry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayConsumer {

    private DelayQueue<DelayTask<Result>> delayQueue;
    private Executor executor;
    private static final long fixDelaySec = 5;
    private static final int retryLimit = 3;
    private ResultChecker<Result> resultChecker = new DefaultResultChecker();

    public DelayConsumer(DelayQueue<DelayTask<Result>> delayQueue, Executor executor) {
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
        final DelayTask<Result> task;
        try {
            task = delayQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        task.asyncDoTask(executor)
            .whenCompleteAsync((result, e) -> {
                boolean success = resultChecker.checkSuccess(result, e);
                if (!success) {
                    int retryCount = task.getRetryCount();
                    if (retryCount >= retryLimit) {
                        log.error("重试次数为{}，到达上限{}，放弃重试", retryCount, retryLimit);
                        return;
                    }
                    Instant newExpire = task.newRetry(fixDelaySec, ChronoUnit.SECONDS);
                    log.warn("本次任务失败，进行第{}次重试，" +
                            "延迟到{}秒后。", task.getRetryCount(), LocalDateTime.ofInstant(newExpire, ZoneId.systemDefault()));
                    delayQueue.offer(task);
                }
            }, executor);
    }

    @Slf4j
    private static class DefaultResultChecker implements ResultChecker<Result> {

        @Override
        public boolean checkSuccess(Result result, Throwable e) {
            if (e == null && result != null && result.isSuccess()) {
                log.info("本次任务成功");                
                return true;
            }
            log.warn("本次任务失败", e);
            return false;
        }
    }
}
