package ru.otus;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.service.ClientService;
import ru.otus.service.handlers.GetAllClientsRequestHandler;
import ru.otus.service.handlers.GetAllClientsResponseHandler;
import ru.otus.service.handlers.SaveClientRequestHandler;
import ru.otus.service.handlers.SaveClientResponseHandler;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ClientService clientService;

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("dbMsClient")
    public MsClient dbMsClient() {
        return new MsClientImpl("dbMsClient", messageSystem(), dbHandlersStore(), callbackRegistry());
    }

    @Bean("frontMsClient")
    public MsClient frontMsClient() {
        return new MsClientImpl("frontMsClient", messageSystem(), frontHandlersStore(), callbackRegistry());
    }

    @Bean
    SaveClientResponseHandler saveClientResponseHandler() {
        return new SaveClientResponseHandler(callbackRegistry());
    }

    @Bean
    SaveClientRequestHandler saveClientRequestHandler() {
        return new SaveClientRequestHandler(clientService);
    }

    @Bean
    GetAllClientsResponseHandler getAllClientsResponseHandler() {
        return new GetAllClientsResponseHandler(callbackRegistry());
    }

    @Bean
    GetAllClientsRequestHandler getAllClientsRequestHandler() {
        return new GetAllClientsRequestHandler(clientService);
    }

    HandlersStore dbHandlersStore() {
        HandlersStore dbHandlersStore = new HandlersStoreImpl();
        dbHandlersStore.addHandler(MessageType.CLIENT_DATA, saveClientRequestHandler());
        dbHandlersStore.addHandler(MessageType.CLIENT_LIST_DATA, getAllClientsRequestHandler());
        return dbHandlersStore;
    }

    HandlersStore frontHandlersStore() {
        HandlersStore frontendHandlerStore = new HandlersStoreImpl();
        frontendHandlerStore.addHandler(MessageType.CLIENT_DATA, saveClientResponseHandler());
        frontendHandlerStore.addHandler(MessageType.CLIENT_LIST_DATA, getAllClientsResponseHandler());
        return frontendHandlerStore;
    }

    @PostConstruct
    public void initMessageSystem() {
        messageSystem().addClient(frontMsClient());
        messageSystem().addClient(dbMsClient());
    }
}
