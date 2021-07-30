package ru.otus.equation.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.equation.services.*;

@Configuration
public class AppConfig1 {
    @Bean(name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @Bean(name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
}
