package ru.otus.hibernate.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.hibernate.crm.model.Address;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.Phone;
import ru.otus.hibernate.crm.service.DbServiceClient;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        Context context = new ContextImpl(AppConfig.class);
        var dbServiceClient = context.getBean(DbServiceClient.class);

        //Creating client 1
        Address address1 = new Address("Street 1");
        Phone phone1 = new Phone("1 phone number");
        Client client1 = new Client("client 1", address1, List.of(phone1));
        address1.setClient(client1);
        phone1.setClient(client1);
        //Saving client 1
        dbServiceClient.saveClient(client1);
        //Creating client 2
        Address address2 = new Address("Street 2");
        Phone phone2 = new Phone("2 phone number");
        Client client2 = new Client("client 2", address2, List.of(phone2));
        address2.setClient(client2);
        phone2.setClient(client2);
        //Saving client 2
        var client2Saved = dbServiceClient.saveClient(client2);

        var client2Selected = dbServiceClient.getClient(client2Saved.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client2Saved.getId()));
        log.info("clientSecondSelected:{}", client2Selected);
        //Updating client 2
        Address address2Updated = new Address(client2Selected.getAddress().getId(), "Street 2 updated");
        Phone phone2Updated = new Phone(client2Selected.getPhones().get(0).getId(), "2 phone number updated");
        Phone phone2New = new Phone("2 phone number new");
        Client client2Updated = new Client(client2Selected.getId(), "client 2 updated",
                address2Updated, List.of(phone2Updated, phone2New));
        address2Updated.setClient(client2Updated);
        phone2Updated.setClient(client2Updated);
        //Saving updated client 2
        dbServiceClient.saveClient(client2Updated);

        var clientUpdated = dbServiceClient.getClient(client2Selected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client2Selected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
