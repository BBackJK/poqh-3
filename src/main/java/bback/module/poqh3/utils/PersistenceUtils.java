package bback.module.poqh3.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PersistenceUtils {

    private PersistenceUtils() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    public static List<String> getJpqlColumns(Class<?> entityType) {
        if (entityType == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        List<Field> columnList = getColumnFields(entityType);

        int columnCount = columnList.size();
        for (int i=0; i<columnCount; i++) {
            Field field = columnList.get(i);
            result.add(field.getName());
        }

        return result;
    }

    public static List<String> getNativeColumns(Class<?> entityType) {
        if (entityType == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        List<Field> columnList = getColumnFields(entityType);

        int columnCount = columnList.size();
        for (int i=0; i<columnCount; i++) {
            Field field = columnList.get(i);
            Column column = field.getAnnotation(Column.class);
            String columnName = Strings.camel2Under(field.getName());
            if ( column != null && !column.name().isEmpty() ) {
                columnName = column.name();
            }
            result.add(columnName);
        }

        return result;
    }

    public static List<Field> getColumnFields(Class<?> entityType) {
        if (entityType == null) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>();

        Field[] fields = entityType.getDeclaredFields();
        int fieldCount = fields.length;
        for (int i=0; i<fieldCount; i++) {
            Field f = fields[i];
            Transient transientField = f.getAnnotation(Transient.class);
            if ( transientField != null ) continue;
            result.add(f);
        }

        return result;
    }

    public static String getTableName(Class<?> classType) {
        if (classType == null) {
            return null;
        }
        String result = Strings.camel2Under(classType.getSimpleName());
        Table table = classType.getAnnotation(Table.class);
        if (table != null && !table.name().isEmpty()) {
            result = table.name();
        }

        return result;
    }

    public static boolean isEntityClass(Class<?> classType) {
        if (classType == null) {
            return false;
        }
        return classType.getAnnotation(Entity.class) != null;
    }
}
