package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.ComponentScan;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.model.ServiceA;
import ru.otus.container.model.ServiceAImpl;

@Configuration
@ComponentScan(basePackages = "ru.otus.container.model.components")
public class ComponentConfig {
    @Bean
    public ServiceA serviceA() {
        return new ServiceAImpl();
    }
}
