package ru.otus.container.transactional;

import lombok.*;
import org.hibernate.Session;

import java.sql.Connection;

@Getter
@Setter
@Builder
public class TransactionObject {
    private TransactionType type;
//
//    private Session session;
//
//    private Connection connection;

    private Propagation propagation;

    private Isolation isolation;
}
