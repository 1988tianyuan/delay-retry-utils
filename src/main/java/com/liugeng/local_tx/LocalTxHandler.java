package com.liugeng.local_tx;

public interface LocalTxHandler {

    void doHandle();

    void rollback(Throwable e);

    void doTask() throws Exception;

    void internalRollback(Throwable e);

    void setNext(LocalTxHandler nextHandler);

    void setPre(LocalTxHandler preHandler);
}
