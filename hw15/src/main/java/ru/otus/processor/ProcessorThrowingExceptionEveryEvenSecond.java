package ru.otus.processor;

import ru.otus.TimeProvider;
import ru.otus.model.Message;

public class ProcessorThrowingExceptionEveryEvenSecond implements Processor {

    private TimeProvider timeProvider;

    public ProcessorThrowingExceptionEveryEvenSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        if (timeProvider.getCurrentTime().getSecond() % 2 == 0)
            throw new RuntimeException("Even second exception");

        return message;
    }
}
