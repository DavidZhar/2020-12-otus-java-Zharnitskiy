package ru.otus.listener;

import ru.otus.model.Message;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListenerHistory implements Listener {
    private final Map<Message, Message> history = new LinkedHashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        history.put(oldMsg, newMsg);
    }

    public Map<Message, Message> getHistory() {
        return history;
    }
}
