package ru.otus.container.service.transactional;

import ru.otus.container.annotation.Transactional;

import java.util.concurrent.Callable;

public interface ServiceTransactionalA {
    <V> V transactionalMethod(Callable<V> action) throws Exception;
}
