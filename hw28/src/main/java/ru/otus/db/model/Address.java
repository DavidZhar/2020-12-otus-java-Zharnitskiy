package ru.otus.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Data
public class Address {
    @Id
    private Long id;

    private String street;

    private Long clientId;

}
