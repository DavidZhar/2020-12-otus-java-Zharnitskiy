package ru.otus.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.model.client.Client;
import ru.otus.model.client.ClientListData;

@Controller
public class ClientController {

    private final MsClient msClient;
    private final SimpMessagingTemplate template;

    public ClientController(@Qualifier("frontMsClient") MsClient msClient, SimpMessagingTemplate template) {
        this.msClient = msClient;
        this.template = template;
    }

    @MessageMapping("/message/client/save")
    public void save(@RequestBody Client client) {
        Message outMsg = msClient.produceMessage("dbMsClient", client,
                MessageType.CLIENT_DATA, c -> {});
        msClient.sendMessage(outMsg);
    }

    @MessageMapping("/message/client/getAll")
    public void getAll() {
        Message outMsg = msClient.produceMessage("dbMsClient", new ClientListData(),
                MessageType.CLIENT_LIST_DATA, this::sendAll);
        msClient.sendMessage(outMsg);
    }

    private void sendAll(ClientListData clientListData) {
        template.convertAndSend("/topic/clients/all", clientListData.getData());
    }
}