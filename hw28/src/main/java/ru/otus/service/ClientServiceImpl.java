package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.db.model.Client;
import ru.otus.db.repo.ClientRepository;
import ru.otus.db.transaction.TransactionExecutor;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final TransactionExecutor transactionExecutor;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(TransactionExecutor transactionExecutor, ClientRepository clientRepository) {
        this.transactionExecutor = transactionExecutor;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return transactionExecutor.doInTransaction(() -> clientRepository.save(client));
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        return clientList;
    }
}
