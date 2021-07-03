package ru.otus.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Data
public class Phone {

    @Id
    private Long id;

    private String number;

    private Long clientId;

}
