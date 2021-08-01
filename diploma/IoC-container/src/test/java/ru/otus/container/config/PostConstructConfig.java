package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.model.ServiceA;
import ru.otus.container.model.ServiceAImpl;
import ru.otus.container.model.ServiceB;
import ru.otus.container.model.ServiceBImpl;

@Configuration
public class PostConstructConfig {
    @Bean
    public ServiceA serviceA() {
        return new ServiceAImpl();
    }

    @Bean
    public ServiceB serviceB(ServiceA serviceA) {
        return new ServiceBImpl(serviceA);
    }
}
