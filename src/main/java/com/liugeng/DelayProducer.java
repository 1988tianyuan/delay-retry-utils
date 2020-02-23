package com.liugeng;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayProducer {

    private DelayQueue<DelayTask> delayQueue;
    private static final Instant fixExpire = Instant.now().plus(5, ChronoUnit.SECONDS);

    public DelayProducer(DelayQueue<DelayTask> delayQueue) {
        this.delayQueue = delayQueue;
    }

    public void produce() {
        new Thread(() -> {
//            while (true) {
                log.info("添加任务");
                DelayTask delayTask = new DelayTask(fixExpire, () -> {
                    boolean success = RandomUtils.nextBoolean();
                    return "FAILED";
                });
                delayQueue.offer(delayTask);
                    try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }).start();
    }
}
