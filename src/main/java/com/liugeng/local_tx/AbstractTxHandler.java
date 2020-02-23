package com.liugeng.local_tx;

public abstract class AbstractTxHandler implements LocalTxHandler {

    private LocalTxHandler nextHandler;

    private LocalTxHandler preHandler;

    @Override
    public void doHandle() {
        try {
            doTask();
        } catch (Throwable e) {
            internalRollback(e);
            return;
        }
        if (nextHandler != null) {
            nextHandler.doHandle();
        }
    }

    @Override
    public void internalRollback(Throwable e) {
        rollback(e);
        if (preHandler != null) {
            preHandler.internalRollback(e);
        }
    }

    @Override
    public void setNext(LocalTxHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void setPre(LocalTxHandler preHandler) {
        this.preHandler = preHandler;
    }
}
