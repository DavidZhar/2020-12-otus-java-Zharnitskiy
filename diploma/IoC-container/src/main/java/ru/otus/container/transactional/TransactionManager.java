package ru.otus.container.transactional;

import java.util.concurrent.Callable;

public interface TransactionManager {

    <T> T doInTransaction(TransactionObject transactionObject, Callable<T> action) throws Exception;

}
