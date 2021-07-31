package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.model.ServiceB;
import ru.otus.container.model.ServiceBImpl;

@Configuration
public class PostConstructConfig {
    @Bean
    public ServiceB serviceB() {
        return new ServiceBImpl();
    }
}
