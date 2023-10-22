package bback.module.poqh3.utils;

import jakarta.persistence.*;

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
            if ( column != null && !column.name().isEmpty() ) {
                result.add(column.name());
                continue;
            }

            Field relationField = getRelationField(entityType, field.getType());
            if ( relationField != null ) {
                // 관계를 설정할 때 JoinColumns 을 이용한 경우
                JoinColumns joinColumns = relationField.getAnnotation(JoinColumns.class);
                if (joinColumns != null) {
                    for (JoinColumn jc : joinColumns.value()) {
                        if (!jc.referencedColumnName().isEmpty()) {
                            result.add(jc.referencedColumnName().toLowerCase());
                        } else if (!jc.name().isEmpty()){
                            result.add(jc.name().toLowerCase());
                        }
                    }
                } else {
                    // 관계를 설정할 때 JoinColumn 을 이용한 경우
                    JoinColumn joinColumn = relationField.getAnnotation(JoinColumn.class);
                    if ( joinColumn != null && !joinColumn.name().isEmpty() ) {
                        result.add(joinColumn.name().toLowerCase());
                    } else {
                        // 관계를 설정할 때 joinColumn 없이, 그냥 ManyToOne 혹은 OneToOne 어노테이션만 사용하여 관계를 맺은 경우 (==> 해당 객체의 ID 값들이 foreign Key 로 반영)
                        List<Field> primaryFieldList = getPrimaryFieldList(relationField.getType());
                        int primaryFieldCount = primaryFieldList.size();
                        for (int j=0; j<primaryFieldCount;j++) {
                            Field primaryField = primaryFieldList.get(j);
                            Column primaryColumn = primaryField.getAnnotation(Column.class);
                            String primaryFieldName = String.format("%s_%s", Strings.camel2Under(relationField.getName()), Strings.camel2Under(primaryField.getName()));
                            if ( primaryColumn != null && !primaryColumn.name().isEmpty() ) {
                                primaryFieldName = String.format("%s_%s", Strings.camel2Under(relationField.getName()), primaryColumn.name());
                            }
                            result.add(primaryFieldName);
                        }
                    }
                }
                continue;
            }

            result.add(Strings.camel2Under(field.getName()));
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

            if ( !isEmbeddedField(f) ) {
                result.add(f);
                continue;
            }

            Class<?> embeddedColumnType = f.getType();
            List<Field> embeddedColumnFieldList = getColumnFields(embeddedColumnType);
            result.addAll(embeddedColumnFieldList);
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

    public static List<Field> getForeignFieldList(Class<?> sourceEntity) {
        List<Field> result = new ArrayList<>();
        List<Field> fieldList = getColumnFields(sourceEntity);
        for (Field f : fieldList) {
            ManyToOne manyToOne = f.getAnnotation(ManyToOne.class);
            OneToOne oneToOne = f.getAnnotation(OneToOne.class);
            if (
                    manyToOne != null
                            || (oneToOne != null && oneToOne.mappedBy().isEmpty())
            ) {
                result.add(f);
            }
        }

        return result;
    }

    public static Field getRelationField(Class<?> sourceEntity, Class<?> targetEntity) {
        if (sourceEntity == null || targetEntity == null) {
            return null;
        }

        Field field = null;
        List<Field> columnFieldList = getColumnFields(sourceEntity);
        for (Field f : columnFieldList) {
            if (f.getType().equals(targetEntity)) {
                ManyToOne manyToOne = f.getAnnotation(ManyToOne.class);
                OneToOne oneToOne = f.getAnnotation(OneToOne.class);
                if (manyToOne != null || oneToOne != null) {
                    field = f;
                    break;
                }
            }
        }

        return field;
    }

    public static List<Field> getRelationFields(Class<?> entityType) {
        if (!isEntityClass(entityType)) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>();
        List<Field> columnFieldList = getColumnFields(entityType);
        for (Field f : columnFieldList) {
            JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                result.add(f);
            }
        }

        return result;
    }

    public static List<Field> getPrimaryFieldList(Class<?> sourceEntity) {
        List<Field> result = new ArrayList<>();
        List<Field> fieldList = getColumnFields(sourceEntity);
        for (Field f : fieldList) {
            Id id = f.getAnnotation(Id.class);
            if ( id != null ) {
                result.add(f);
                continue;
            }

            EmbeddedId embeddedId = f.getAnnotation(EmbeddedId.class);
            if ( embeddedId != null ) {
                result.addAll(getColumnFields(f.getType()));
            }
        }
        return result;
    }

    public static boolean isEntityClass(Class<?> classType) {
        if (classType == null) {
            return false;
        }
        return classType.getAnnotation(Entity.class) != null;
    }

    public static boolean isRelationEntity(Class<?> sourceEntity, Class<?> targetEntity) {
        return getRelationField(sourceEntity, targetEntity) != null;
    }

    private static boolean isEmbeddedField(Field f) {
        boolean result = false;
        Embedded embedded = f.getAnnotation(Embedded.class);
        if (embedded != null) {
            Class<?> columnType = f.getType();
            Embeddable embeddable = columnType.getAnnotation(Embeddable.class);
            if (embeddable != null) {
                result = true;
            }
        }
        return result;
    }
}
