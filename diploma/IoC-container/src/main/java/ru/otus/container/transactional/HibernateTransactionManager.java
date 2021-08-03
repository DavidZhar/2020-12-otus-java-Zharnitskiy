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

            case REQUIRED:
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = session.getTransaction();
                if (transaction == null || transaction.getStatus() == TransactionStatus.ACTIVE) {
                    result = action.call();
                } else {
                    result = createNewTransactionAndExecute(action, isolation, session);
                }
                break;

            case SUPPORTS:
                result = action.call();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + propagation);
        }
        return result;
    }

    private <T> T createNewTransactionAndExecute(Callable<T> action, Isolation isolation, Session session) throws Exception {
        var transaction = session.beginTransaction();
        return executeAndCommitOrRollback(action, isolation, session, transaction);
    }

    private <T> T executeAndCommitOrRollback(Callable<T> action, Isolation isolation, Session session, Transaction transaction) throws Exception {
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
