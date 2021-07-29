package ru.otus.service.handlers;

import lombok.RequiredArgsConstructor;
import ru.otus.model.client.Client;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.service.ClientService;

import java.util.Optional;

@RequiredArgsConstructor
public class SaveClientRequestHandler implements RequestHandler<Client> {
    private final ClientService clientService;

    @Override
    public Optional<Message> handle(Message msg) {
        Client client = MessageHelper.getPayload(msg);
        Client saved = clientService.save(client);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, saved));
    }
}
