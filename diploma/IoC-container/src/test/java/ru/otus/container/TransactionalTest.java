package ru.otus.container;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.container.config.TransactionalConfig;
import ru.otus.container.core.Context;
import ru.otus.container.core.ContextImpl;
import ru.otus.container.model.User;
import ru.otus.container.service.transactional.ServiceTransactional;

public class TransactionalTest {

    @Test
    void shouldOpenAndCloseTransaction() throws Exception {
        Context context = new ContextImpl(TransactionalConfig.class);

        ServiceTransactional bean = context.getBean(ServiceTransactional.class);
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);

        Transaction transactionClosed = bean.transactionalMethod(() -> {
            Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
            Assertions.assertTrue(transaction.isActive());
            return transaction;
        });

        Assertions.assertFalse(transactionClosed.isActive());
    }

    @Test
    void shouldRollbackInCaseOfException() throws Exception {
        Context context = new ContextImpl(TransactionalConfig.class);

        ServiceTransactional bean = context.getBean(ServiceTransactional.class);
        SessionFactory sessionFactory = context.getBean(SessionFactory.class);

        User newUser = bean.transactionalMethod(() -> {
            User user = new User();
            user.setName("User new");
            Session session = sessionFactory.getCurrentSession();
            session.persist(user);
            return user;
        });

        String expectedName = "Expected name";

        bean.transactionalMethod(() -> {
            User user = new User();
            user.setId(newUser.getId());
            user.setName(expectedName);
            Session session = sessionFactory.getCurrentSession();
            session.merge(user);
            return user;
        });

        try {
            bean.transactionalMethod(() -> {
                User user = new User();
                user.setId(newUser.getId());
                user.setName("Updated unexpected");
                Session session = sessionFactory.getCurrentSession();
                session.merge(user);
                throw new RuntimeException();
            });
        } catch (Exception ignored) {
        }

        User updatedUser = bean.transactionalMethod(() -> {
            User user = new User();
            user.setId(newUser.getId());
            user.setName("Updated unexpected");
            Session session = sessionFactory.getCurrentSession();
            return session.find(User.class, newUser.getId());
        });

        Assertions.assertEquals(expectedName, updatedUser.getName());
    }

}
