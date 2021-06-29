package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl implements EntityClassMetaData {
    private final Class<?> clazz;
    private final Constructor<?> constructor;
    private final Field idField;
    private final List<Field> fields;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;
        try {
            this.constructor = clazz.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException("No public constructor in the entity!!!");
        }
        try {
            this.idField = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
        } catch (Exception e){
            throw new RuntimeException("No Id field in the entity!!!");
        }
        try {
            this.fields = Arrays.asList(clazz.getDeclaredFields());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public <T> Constructor<T> getConstructor() {
        return (Constructor<T>) constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fields.stream().filter(f -> !f.equals(idField)).collect(Collectors.toList());
    }
}
