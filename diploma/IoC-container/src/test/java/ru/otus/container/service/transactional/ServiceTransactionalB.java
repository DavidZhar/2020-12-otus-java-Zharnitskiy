package ru.otus.container.service.transactional;

import java.util.concurrent.Callable;

public interface ServiceTransactionalB {
    <V> V transactionalMethod(Callable<V> action) throws Exception;

}
