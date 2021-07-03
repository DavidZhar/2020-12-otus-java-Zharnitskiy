package ru.otus.service;

import ru.otus.db.model.Client;

import java.util.List;

public interface ClientService {

    Client save(Client client);

    List<Client> findAll();
}
