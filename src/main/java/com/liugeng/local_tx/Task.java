package com.liugeng.local_tx;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/3/16 13:09
 */
@FunctionalInterface
public interface Task<D> {
	
	void doTask(D TxContext) throws Throwable;
}
