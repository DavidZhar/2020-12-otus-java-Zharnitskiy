package ru.otus.container.service.transactional;

import ru.otus.container.annotation.Transactional;

import java.util.concurrent.Callable;

public class ServiceTransactionalBImpl implements ServiceTransactionalB {

    @Override
    @Transactional
    public <V> V transactionalMethod(Callable<V> action) throws Exception {
        return action.call();
    }
}
