package ru.otus.db.core.sessionmanager;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
