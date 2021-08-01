package ru.otus.container.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.core.BeanPostProcessor;

@Configuration
public class CustomBeanPostProcessorConfig {

    @Bean
    BeanPostProcessor customBeanPostProcessor(){
        return new CustomBeanPostProcessor();
    }
}
