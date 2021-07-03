package ru.otus.db.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.db.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
