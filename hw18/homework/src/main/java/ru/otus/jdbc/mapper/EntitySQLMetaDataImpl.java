package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData entityClassMetaData;
    private final String tableName;
    private final String id;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.tableName = entityClassMetaData.getName().toLowerCase();
        this.id = entityClassMetaData.getIdField().getName();
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + tableName;
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from " + tableName + " where " + id + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fieldsWithoutIdSQL = fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(", "));
        String params = fieldsWithoutId.size() == 1 ? "?" : fieldsWithoutId.stream().map(f -> "?")
                .collect(Collectors.joining(", "));
        return "insert into " + tableName + "(" + fieldsWithoutIdSQL + ") values (" + params + ")";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String params = fieldsWithoutId.stream().map(f -> f.getName() + " = ?").collect(Collectors.joining(", "));
        return "update " + tableName + " set " + params + "where " + id + " = ?";
    }
}
