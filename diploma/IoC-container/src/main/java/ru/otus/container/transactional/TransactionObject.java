package ru.otus.container.transactional;

import lombok.*;

@Getter
@Setter
@Builder
public class TransactionObject {
    private TransactionType type;  // Currently only Hibernate transactions are supported

    private Propagation propagation;

    private Isolation isolation;
}
