package com.liugeng.retry;

public interface ResultChecker<R> {

    boolean checkSuccess(R result, Throwable e);
}
