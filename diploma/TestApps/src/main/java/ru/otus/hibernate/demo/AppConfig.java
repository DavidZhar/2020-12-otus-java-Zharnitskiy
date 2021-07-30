package ru.otus.hibernate.demo;

import org.hibernate.SessionFactory;
import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.annotation.EnableHibernateTransactionManagement;
import ru.otus.hibernate.core.repository.DataTemplateHibernate;
import ru.otus.hibernate.crm.model.Address;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.Phone;
import ru.otus.hibernate.crm.service.DbServiceClient;
import ru.otus.hibernate.crm.service.DbServiceClientImpl;

@Configuration
@EnableHibernateTransactionManagement(annotatedClasses = {Client.class, Address.class, Phone.class})
public class AppConfig {

    @Bean
    public DbServiceClient dbServiceClient(SessionFactory sessionFactory) {
        return new DbServiceClientImpl(new DataTemplateHibernate<>(Client.class), sessionFactory);
    }
}
