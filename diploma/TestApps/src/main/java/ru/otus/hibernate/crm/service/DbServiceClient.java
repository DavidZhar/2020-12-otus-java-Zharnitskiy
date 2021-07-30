package ru.otus.hibernate.crm.service;

import ru.otus.hibernate.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DbServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
