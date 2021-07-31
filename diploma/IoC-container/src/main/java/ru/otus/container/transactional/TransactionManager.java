package ru.otus.container.transactional;

import java.util.concurrent.Callable;

public interface TransactionManager {

//    TransactionObject getTransaction();
//
//    void begin(TransactionObject transactionObject);
//
//    void commit(TransactionObject transactionObject);
//
//    void rollback(TransactionObject transactionObject);

    <T> T doInTransaction(TransactionObject transactionObject, Callable<T> action) throws Exception;

}
