package com.liugeng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandRunnerImpl implements CommandLineRunner {
    @Autowired
    private Test test;

    @Override
    public void run(String... args) throws InterruptedException {
        test.doDelayTask();
        Thread.sleep(Long.MAX_VALUE);
    }
}
