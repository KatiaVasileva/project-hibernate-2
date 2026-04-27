package com.vasileva.config;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;

public class TransactionExecutor {
    private final SessionCreator sessionCreator;

    public TransactionExecutor(SessionCreator sessionCreator) {
        this.sessionCreator = sessionCreator;
    }

    public void execute(Runnable action) {
        Session session = sessionCreator.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            action.run();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public <T> T executeWithResult(Supplier<T> action) {
        Session session = sessionCreator.getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {
            T result = action.get();
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}
