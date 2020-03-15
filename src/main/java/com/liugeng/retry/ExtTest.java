package com.liugeng.retry;

import com.google.common.base.Predicate;
import com.liugeng.retry.extension.*;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

public class ExtTest {

    public static void main(String[] args) throws IOException, ExecutionException, RetryException, InterruptedException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        threadPool.allowCoreThreadTimeOut(true);
        Retryer<Result> retryer = RetryerBuilder.<Result>newBuilder()
                .retryIfResult(result -> result == null || !result.isSuccess())
                .retryIfExceptionOfType(NeedRetryException.class)
                .withWaitStrategy(WaitStrategies
                        .incrementingWait(1, TimeUnit.SECONDS, 2, TimeUnit.SECONDS))
                .withBlockStrategy(BlockStrategies.threadSleepStrategy())
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(10, TimeUnit.SECONDS, threadPool))
                .build();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        retryer.call(() -> {
            int randomInt = random.nextInt(20);
            System.out.println("random is " + randomInt);
            if (randomInt > 10) {
                System.out.println("success");
                return new Result(true);
            }
            System.out.println("fail");
            return new Result(false);
        });
        threadPool.shutdown();
    }
}
