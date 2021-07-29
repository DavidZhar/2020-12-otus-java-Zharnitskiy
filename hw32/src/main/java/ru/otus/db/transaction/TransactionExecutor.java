package ru.otus.db.transaction;

public interface TransactionExecutor {

    <T> T doInTransaction(TransactionAction<T> action);
}
