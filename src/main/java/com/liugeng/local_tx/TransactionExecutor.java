package com.liugeng.local_tx;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/3/16 10:53
 */
public class TransactionExecutor<D> {
	private TransactionHandler<D> header;
	private TransactionHandler<D> tail;
	
	public void begin(D TxContext) throws Throwable {
		if (header != null) {
			header.doHandle(TxContext);
		}
	}
	
	public static <D> TransactionExecutor<D> init() {
		return new TransactionExecutor<>();
	}
	
	public TransactionExecutor<D> addHandler(Rollback<D> rollback, Task<D> task) {
		TransactionHandlerImpl<D> handler = new TransactionHandlerImpl<>(rollback, task);
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
