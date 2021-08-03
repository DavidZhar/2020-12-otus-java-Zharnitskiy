package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.ComponentScan;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.service.ServiceA;
import ru.otus.container.service.ServiceAImpl;

@Configuration
@ComponentScan(basePackages = "ru.otus.container.service.components")
public class ComponentConfig {
    @Bean
    public ServiceA serviceA() {
        return new ServiceAImpl();
    }
}
