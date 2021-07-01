package ru.otus.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.cfg.Configuration;
import ru.otus.db.core.repository.DataTemplateHibernate;
import ru.otus.db.core.repository.HibernateUtils;
import ru.otus.db.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.db.crm.model.Address;
import ru.otus.db.crm.model.Client;
import ru.otus.db.crm.model.Phone;
import ru.otus.db.crm.service.DBServiceClient;
import ru.otus.db.crm.service.DbServiceClientImpl;
import ru.otus.web.template.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private final TemplateProcessor templateProcessor;
    private DBServiceClient dbServiceClient;

    public ClientServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        initContext();
    }

    private void initContext() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        this.dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        addDemoClients();
    }

    private void addDemoClients() {
        for (int i = 1; i < 6; i++) {
            Address address = new Address("Street " + i);
            Phone phone1 = new Phone("+799" + i + (int) (Math.random() * (9999999 - 1000000) + 1000000));
            Phone phone2 = new Phone("+799" + i + (int) (Math.random() * (9999999 - 1000000) + 1000000));
            Client client = new Client("Client " + i, address, List.of(phone1, phone2));
            address.setClient(client);
            phone1.setClient(client);
            phone2.setClient(client);
            dbServiceClient.saveClient(client);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, dbServiceClient.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String street = request.getParameter("address");
        List<String> phoneNumbers = Arrays.asList(request.getParameter("phones").split(","));

        Address address = new Address(street);
        List<Phone> phones = phoneNumbers.stream().map(Phone::new).collect(Collectors.toList());
        Client client = new Client(name, address, phones);
        address.setClient(client);
        phones.forEach(p -> p.setClient(client));
        dbServiceClient.saveClient(client);
        response.sendRedirect("/client");
    }

}
