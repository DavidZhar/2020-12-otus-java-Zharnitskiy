package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.annotation.EnableHibernateTransactionManagement;
import ru.otus.container.model.User;
import ru.otus.container.service.transactional.ServiceTransactionalA;
import ru.otus.container.service.transactional.ServiceTransactionalAImpl;
import ru.otus.container.service.transactional.ServiceTransactionalB;
import ru.otus.container.service.transactional.ServiceTransactionalBImpl;

@Configuration
@EnableHibernateTransactionManagement(annotatedClasses = User.class)
public class TransactionalConfig {

    @Bean
    ServiceTransactionalA serviceTransactionalA() {
        return new ServiceTransactionalAImpl();
    }

    @Bean
    ServiceTransactionalB serviceTransactionalB() {
        return new ServiceTransactionalBImpl();
    }
}
