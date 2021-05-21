package ru.otus.listener;

import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener {
    private final Map<Message, Message> history = new LinkedHashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        oldMsg = getCopyOfMessage(oldMsg);
        newMsg = getCopyOfMessage(newMsg);

        history.put(oldMsg, newMsg);
    }

    private Message getCopyOfMessage(Message message) {
        var objFromMessage = message.getField13();
        if (objFromMessage != null) {
            var objForMessage = new ObjectForMessage();
            objForMessage.setData(objFromMessage.getData());
            message = message.toBuilder().field13(objForMessage).build();
        }
        return message;
    }

    public Map<Message, Message> getHistory() {
        return history;
    }

    public Optional<Message> findMessageById(long id) {
        return history.keySet().stream().filter(message -> message.getId() == id).findFirst();
    }
}
