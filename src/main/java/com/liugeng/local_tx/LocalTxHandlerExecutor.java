package com.liugeng.local_tx;

public class LocalTxHandlerExecutor {

    private LocalTxHandler header;
    private LocalTxHandler tail;

    public void begin() {
        if (header != null) {
            header.doHandle();
        }
    }

    public static LocalTxHandlerExecutor init() {
        return new LocalTxHandlerExecutor();
    }

    public LocalTxHandlerExecutor addHandler(LocalTxHandler handler) {
        if (header == null) {
            header = handler;
            tail = header;
        } else {
            tail.setNext(handler);
            handler.setPre(tail);
            tail = handler;
        }
        return this;
    }
}
