package ru.otus.equation;

import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.equation.config.AppConfig;
import ru.otus.equation.config.AppConfig1;
import ru.otus.equation.config.AppConfig2;
import ru.otus.equation.services.GameProcessor;
import ru.otus.equation.services.GameProcessorImpl;

public class App {

    public static void main(String[] args) throws Exception {
        Context context = new ContextImpl(AppConfig.class);
//        Context context = new ContextImpl("ru.otus.equation.config");
//        Context context = new ContextImpl(AppConfig1.class, AppConfig2.class);
//        Context context = new ContextImpl(new String[]{"ru.otus.equation.config"});

//        GameProcessor gameProcessor = context.getBean(GameProcessor.class);
        GameProcessor gameProcessor = context.getBean(GameProcessorImpl.class);
//        GameProcessor gameProcessor = context.getBean("gameProcessor");

        gameProcessor.startGame();
    }
}
