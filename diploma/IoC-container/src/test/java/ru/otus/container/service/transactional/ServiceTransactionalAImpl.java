package ru.otus.container.service.transactional;

import ru.otus.container.annotation.Transactional;

import java.util.concurrent.Callable;

public class ServiceTransactionalAImpl implements ServiceTransactionalA {

    @Override
    @Transactional
    public <V> V transactionalMethod(Callable<V> action) throws Exception {
        return action.call();
    }
}
