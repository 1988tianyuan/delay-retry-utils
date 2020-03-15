package com.liugeng.retry;

public class NeedRetryException extends RuntimeException {
    public NeedRetryException(String message) {
        super(message);
    }

    public NeedRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
