package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.annotation.EnableHibernateTransactionManagement;
import ru.otus.container.model.User;
import ru.otus.container.service.transactional.ServiceTransactional;
import ru.otus.container.service.transactional.ServiceTransactionalImpl;

@Configuration
@EnableHibernateTransactionManagement(annotatedClasses = User.class)
public class TransactionalConfig {

    @Bean
    ServiceTransactional serviceTransactional() {
        return new ServiceTransactionalImpl();
    }
}
