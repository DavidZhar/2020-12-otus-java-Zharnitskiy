package ru.otus.model.client;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.messagesystem.client.ResultDataType;

@Table("phone")
@Data
public class Phone extends ResultDataType {

    @Id
    private Long id;

    private String number;

    private Long clientId;

}
