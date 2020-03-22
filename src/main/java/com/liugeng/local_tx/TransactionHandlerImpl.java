package com.liugeng.local_tx;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/3/16 10:55
 */

public class TransactionHandlerImpl<D> implements TransactionHandler<D> {
	
	private Rollback<D> rollback;
	
	private Task<D> task;
	
	public TransactionHandlerImpl(Rollback<D> rollback, Task<D> task) {
		this.rollback = rollback;
		this.task = task;
	}
	
	private TransactionHandler<D> nextHandler;
	
	private TransactionHandler<D> preHandler;
	
	@Override
	public void doHandle(D TxContext) throws Throwable {
		try {
			task.doTask(TxContext);
		} catch (Throwable e) {
			internalRollback(TxContext, e);
			return;
		}
		if (nextHandler != null) {
			nextHandler.doHandle(TxContext);
		}
	}
	
	// send error to previous handler
	private void passBack(D TxContext, Throwable e) throws Throwable {
		if (preHandler != null) {
			preHandler.internalRollback(TxContext, e);
		}
	}
	
	@Override
	public void internalRollback(D TxContext, Throwable e) throws Throwable {
		rollback.rollback(TxContext, e);
		passBack(TxContext, e);
	}
	
	@Override
	public void setNext(TransactionHandler<D> nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	@Override
	public void setPre(TransactionHandler<D> preHandler) {
		this.preHandler = preHandler;
	}
}
