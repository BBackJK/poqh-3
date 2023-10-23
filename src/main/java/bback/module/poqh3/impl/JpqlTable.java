package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.TableIsOnlyAcceptEntityException;
import bback.module.poqh3.utils.PersistenceUtils;
import bback.module.poqh3.utils.Strings;
import jakarta.persistence.JoinColumn;

import java.lang.reflect.Field;
import java.util.List;

public class JpqlTable implements Table {

    private final Class<?> entityType;

    private final List<Field> foreignFieldList;

    private String alias;

    public JpqlTable(Class<?> entityType, String alias) {
        if (!PersistenceUtils.isEntityClass(entityType)) {
            throw new TableIsOnlyAcceptEntityException();
        }
        this.entityType = entityType;
        this.alias = alias;
        this.foreignFieldList = PersistenceUtils.getForeignFieldList(entityType);
    }


    @Override
    public String toQuery() {
        return this.entityType.getSimpleName();
    }

    @Override
    public void AS(String alias) {
        this.alias = alias;
    }

    @Override
    public void AS(int tableIndex) {
        this.alias = String.format("t%d", tableIndex);
    }

    @Override
    public Column COLUMN(String field, String alias) {
        Field foreignField = this.getForeignFieldBySelectName(field);
        if ( foreignField != null ) {
            field = foreignField.getName();
        }
        return new JpqlColumn(this, field, alias);
    }

    @Override
    public Column[] COLUMNS(String... fields) {
        int attrCount = fields.length;
        Column[] result = new Column[attrCount];
        for (int i=0; i<attrCount; i++) {
            result[i] = COLUMN(fields[i]);
        }
        return result;
    }

    @Override
    public Column[] ALL() {
        List<String> fieldNameList = PersistenceUtils.getJpqlColumns(this.entityType);
        int fieldNameCount = fieldNameList.size();
        Column[] result = new Column[fieldNameCount];
        for (int i=0; i<fieldNameCount;i++) {
            result[i] = COLUMN(fieldNameList.get(i));
        }
        return result;
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public Class<?> getEntityType() {
        return this.entityType;
    }

    @Override
    public boolean isJpql() {
        return true;
    }


    private Field getForeignFieldBySelectName(String selectFieldName) {
        String inputCamelName = Strings.toCamel(selectFieldName);
        String inputUnderName = Strings.camel2Under(selectFieldName);

        for (Field f : this.foreignFieldList) {
            // CamelCase field 문자열 이 foreignField 명과 같을 경우, foreignField 라고 할 수 있다.
            if (f.getName().equals(inputCamelName)) {
                return f;
            }
            // JoinColumn 이 있고, name 이 지정되어 있으면, 그 name 은 under bar case 이고, 이 name 과 inputUnderName 이 같을 시 foreignField 로 간주.
            JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);
            if ( joinColumn != null && !(joinColumn.name().isEmpty()) ) {
                if ( joinColumn.name().equals(inputUnderName) ) {
                    return f;
                }
            }

            // 문자열도 같지 않고, JoinColumn 도 같지 않을 경우
            // 해당 Foreign 후보자 타입의 Primary 필드를 조회하여 Foreign key 를 판단한다.
            List<Field> primaryListOfForeignField = PersistenceUtils.getPrimaryFieldList(f.getType());
            for (Field foreignPKField : primaryListOfForeignField) {
                String underRootIdUnderName = String.format("%s_%s", Strings.camel2Under(f.getName()), Strings.camel2Under(foreignPKField.getName()));
                if (inputUnderName.equals(underRootIdUnderName)) {
                    return f;
                }

                jakarta.persistence.Column column = foreignPKField.getAnnotation(jakarta.persistence.Column.class);
                if (column != null && !column.name().isEmpty()) {
                    underRootIdUnderName = String.format("%s_%s", Strings.camel2Under(f.getName()), Strings.camel2Under(column.name()));
                    if (inputUnderName.equals(underRootIdUnderName)) {
                        return f;
                    }
                }
            }
        }
        return null;
    }
}
