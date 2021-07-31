package ru.otus.container.transactional;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class HibernateTransactionManager implements TransactionManager {

    private final SessionFactory sessionFactory;

    @Override
    public <T> T doInTransaction(TransactionObject transactionObject, Callable<T> action) throws Exception {
        Propagation propagation = transactionObject.getPropagation();
        Isolation isolation = transactionObject.getIsolation();

        T result;

        switch (propagation) {
            case NEW:
                Session session1 = sessionFactory.getCurrentSession();
                result = createNewTransactionAndExecute(action, isolation, session1);
                break;

            case REQUIRED:
                Session session2 = sessionFactory.getCurrentSession();
                Transaction transaction2 = session2.getTransaction();
                if (transaction2.getStatus() == TransactionStatus.ACTIVE) {
                    result = execute(action, isolation, session2, transaction2);
                } else {
                    result = createNewTransactionAndExecute(action, isolation, session2);
                }
                break;

            case SUPPORTS:
                Session session3 = sessionFactory.getCurrentSession();
                Transaction transaction3 = session3.getTransaction();
                if (transaction3.getStatus() == TransactionStatus.ACTIVE) {
                    result = execute(action, isolation, session3, transaction3);
                } else {
                    result = action.call();
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + propagation);
        }
        return result;
    }

    private <T> T createNewTransactionAndExecute(Callable<T> action, Isolation isolation, Session session) throws Exception {
        var transaction = session.beginTransaction();
        return execute(action, isolation, session, transaction);
    }

    private <T> T execute(Callable<T> action, Isolation isolation, Session session, Transaction transaction) throws Exception {
        try {
            setIsolationLevel(isolation, session);
            var result = action.call();
            transaction.commit();
            return result;
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
    }

    private void setIsolationLevel(Isolation isolation, Session session) {
        session.doWork(connection -> connection.setTransactionIsolation(isolation.value()));
    }
}
