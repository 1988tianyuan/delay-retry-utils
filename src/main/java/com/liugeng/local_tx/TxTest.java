package com.liugeng.local_tx;

public class TxTest {

    public static void main(String[] args) throws Throwable {
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        handler2.setNeedError(true);
        TransactionExecutor.init()
                .addHandler(handler1, handler1)
                .addHandler(handler2, handler2)
                .addHandler(handler3, handler3)
                .begin(new Object());
    }
}
