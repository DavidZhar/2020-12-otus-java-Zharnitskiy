package ru.otus.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientListData extends ResultDataType {
    private List<Client> data;
}
