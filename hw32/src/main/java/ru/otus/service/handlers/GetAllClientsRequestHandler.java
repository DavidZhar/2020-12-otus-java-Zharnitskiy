package ru.otus.service.handlers;

import lombok.RequiredArgsConstructor;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.model.client.Client;
import ru.otus.model.client.ClientListData;
import ru.otus.service.ClientService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GetAllClientsRequestHandler implements RequestHandler<ClientListData> {
    private final ClientService clientService;

    @Override
    public Optional<Message> handle(Message msg) {
        List<Client> clients = clientService.findAll();
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new ClientListData(clients)));
    }
}
