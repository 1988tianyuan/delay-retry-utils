package com.liugeng.local_tx;

public class LocalTxTest {

    public static void main(String[] args) {
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        handler1.setNeedError(false);
        handler2.setNeedError(false);
        handler3.setNeedError(true);

        LocalTxHandlerExecutor.init()
                .addHandler(handler1)
                .addHandler(handler2)
                .addHandler(handler3)
                .begin();
    }
}
