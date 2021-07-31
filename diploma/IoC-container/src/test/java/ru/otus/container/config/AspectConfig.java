package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.aop.CustomAspect;
import ru.otus.container.model.ServiceA;
import ru.otus.container.model.ServiceAImpl;

@Configuration
public class AspectConfig {

    @Bean
    public ServiceA serviceA(){
        return new ServiceAImpl();
    }

    @Bean
    CustomAspect aspect(){
        return new TestAspect();
    }

}
