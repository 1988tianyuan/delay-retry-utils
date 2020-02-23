package com.liugeng.retry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.DelayQueue;

@Slf4j
public class DelayProducer {

    private DelayQueue<DelayTask<Result>> delayQueue;
    private static final Instant fixExpire = Instant.now().plus(5, ChronoUnit.SECONDS);

    public DelayProducer(DelayQueue<DelayTask<Result>> delayQueue) {
        this.delayQueue = delayQueue;
    }

    public void produce() {
        new Thread(() -> {
//            while (true) {
                log.info("添加任务");
                DelayTask<Result> delayTask = new DelayTask<>(fixExpire, () -> {
                    boolean success = RandomUtils.nextBoolean();
                    return new Result(false);
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
