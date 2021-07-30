package ru.otus.equation.config;

import ru.otus.container.annotation.Bean;
import ru.otus.container.annotation.Configuration;
import ru.otus.equation.services.*;

@Configuration
public class AppConfig2 {
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
}
