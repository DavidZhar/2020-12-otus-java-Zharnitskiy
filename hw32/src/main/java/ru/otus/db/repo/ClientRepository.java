package ru.otus.db.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.client.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
