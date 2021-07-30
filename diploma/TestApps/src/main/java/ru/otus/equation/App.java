package ru.otus.equation;

import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.equation.config.AppConfig;
import ru.otus.equation.services.GameProcessor;
import ru.otus.equation.services.GameProcessorImpl;

public class App {

    public static void main(String[] args) throws Exception {
//        Context context = new ContextImpl(AppConfig1.class, AppConfig2.class);
//        Context context = new ContextImpl("ru.otus.config");
//        Context context = new ContextImpl(new String[]{"ru.otus.config"});
        Context context = new ContextImpl(AppConfig.class);

        GameProcessor gameProcessor = context.getBean(GameProcessor.class);
//        GameProcessor gameProcessor = context.getBean(GameProcessorImpl.class);
//        GameProcessor gameProcessor = context.getBean("gameProcessor");

        gameProcessor.startGame();
    }
}
