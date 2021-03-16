package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.*;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(new ProcessorSwapFields11And12(),
                new LoggerProcessor(new ProcessorThrowingExceptionEveryEvenSecond(new TimeProvider())));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> System.out.println(ex.getMessage()));
        var listenerHistory = new HistoryListener();
        complexProcessor.addListener(listenerHistory);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println(listenerHistory.getHistory());

        complexProcessor.removeListener(listenerHistory);
    }
}
