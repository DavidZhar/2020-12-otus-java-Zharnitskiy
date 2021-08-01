package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.aop.CustomAspect;
import ru.otus.container.service.ServiceC;
import ru.otus.container.service.ServiceCImpl;

@Configuration
public class AspectConfig {

    @Bean
    public ServiceC serviceA() {
        return new ServiceCImpl();
    }

    @Bean
    CustomAspect aspect() {
        return new TestAspect();
    }

}
