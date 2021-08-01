package ru.otus.container.service.transactional;

import ru.otus.container.annotation.Transactional;

import java.util.concurrent.Callable;

public class ServiceTransactionalImpl implements ServiceTransactional {

    @Override
    @Transactional
    public <V> V transactionalMethod(Callable<V> action) throws Exception {
        return action.call();
    }
}
