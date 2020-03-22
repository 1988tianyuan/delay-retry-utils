package com.liugeng.local_tx;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/3/16 10:54
 */
public interface TransactionHandler<D> {
	void doHandle(D TxContext) throws Throwable;
	
	void internalRollback(D TxContext, Throwable e) throws Throwable;
	
	void setNext(TransactionHandler<D> nextHandler);
	
	void setPre(TransactionHandler<D> preHandler);
}
