package ru.otus.db.transaction;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {
}
