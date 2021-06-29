package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return mapRowToObject(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var result = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            result.add(mapRowToObject(rs));
                        }
                        return result;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    private T mapRowToObject(ResultSet rs) throws Exception {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        constructor.setAccessible(true);
        T result = constructor.newInstance();
        entityClassMetaData.getAllFields().forEach(f -> {
            try {
                f.setAccessible(true);
                f.set(result, rs.getObject(f.getName()));
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
        return result;
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    entityClassMetaData.getFieldsWithoutId().stream().map(f -> {
                        try {
                            f.setAccessible(true);
                            return f.get(client);
                        } catch (Exception e) {
                            throw new DataTemplateException(e);
                        }
                    }).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = entityClassMetaData.getFieldsWithoutId().stream().map(f -> {
                try {
                    f.setAccessible(true);
                    return f.get(client);
                } catch (Exception e) {
                    throw new DataTemplateException(e);
                }
            }).collect(Collectors.toList());
            params.add(entityClassMetaData.getIdField().get(client));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
