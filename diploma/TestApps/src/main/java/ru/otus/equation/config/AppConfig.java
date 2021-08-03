package ru.otus.equation.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.ComponentScan;
import ru.otus.container.annotation.Configuration;
import ru.otus.container.annotation.EnableHibernateTransactionManagement;
import ru.otus.equation.services.*;

@Configuration
@ComponentScan(basePackages = "ru.otus.equation.services")
public class AppConfig {
//
//    @Bean(name = "equationPreparer")
//    public EquationPreparer equationPreparer() {
//        return new EquationPreparerImpl();
//    }
//
//    @Bean(name = "playerService")
//    public PlayerService playerService(IOService ioService) {
//        return new PlayerServiceImpl(ioService);
//    }

    @Bean(name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @Bean(name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }

}
