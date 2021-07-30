package ru.otus.hibernate.crm.service;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.annotation.Autowired;
import ru.otus.container.annotation.Component;
import ru.otus.container.annotation.Transactional;
import ru.otus.hibernate.core.repository.DataTemplate;
import ru.otus.hibernate.crm.model.Client;

import java.util.List;
import java.util.Optional;

@Component
public class DbServiceClientImpl implements DbServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public DbServiceClientImpl(DataTemplate<Client> clientDataTemplate, SessionFactory sessionFactory) {
        this.clientDataTemplate = clientDataTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Client saveClient(Client client) {
        var clientCloned = client.clone();
        if (client.getId() == null) {
            clientDataTemplate.insert(sessionFactory.getCurrentSession(), clientCloned);
            log.info("created client: {}", clientCloned);
            return clientCloned;
        }
        clientDataTemplate.update(sessionFactory.getCurrentSession(), clientCloned);
        log.info("updated client: {}", clientCloned);
        return clientCloned;
    }

    @Override
    @Transactional
    public Optional<Client> getClient(long id) {
        var clientOptional = clientDataTemplate.findById(sessionFactory.getCurrentSession(), id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    @Transactional
    public List<Client> findAll() {
        var clientList = clientDataTemplate.findAll(sessionFactory.getCurrentSession());
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
