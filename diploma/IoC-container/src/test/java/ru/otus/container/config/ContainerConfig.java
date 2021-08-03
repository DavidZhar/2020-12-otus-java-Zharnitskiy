package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.service.ServiceA;
import ru.otus.container.service.ServiceAImpl;
import ru.otus.container.service.ServiceB;
import ru.otus.container.service.ServiceBImpl;

@Configuration
public class ContainerConfig {

    @Bean
    public ServiceA serviceA() {
        return new ServiceAImpl();
    }

    @Bean(name = "serviceBCustomName")
    public ServiceB serviceB(ServiceA serviceA) {
        return new ServiceBImpl(serviceA);
    }

}
