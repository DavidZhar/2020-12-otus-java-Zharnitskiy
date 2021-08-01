package ru.otus.container.service.transactional;

import ru.otus.container.annotation.Transactional;

import java.util.concurrent.Callable;

public interface ServiceTransactional {
    @Transactional
    <V> V transactionalMethod(Callable<V> action) throws Exception;
}
