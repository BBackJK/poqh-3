package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.DeveloperMistakeException;
import bback.module.poqh3.utils.PersistenceUtils;
import bback.module.poqh3.utils.Strings;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 주체는 R
class JpqlForeignTable<T, R> implements Table<R> {

    private final Table<T> root;
    private final Class<R> foreignType;
    private final Field relationField;
    private final List<JpqlColumn<R>> entityColumnList;

    private final Map<Field, List<Field>> foreignPkMap;

    private String alias;

    public JpqlForeignTable(Table<T> root, Class<R> foreignType, String alias) {
        this.root = root;
        this.foreignType = foreignType;
        this.alias = alias;
        this.entityColumnList = PersistenceUtils.getColumnFields(foreignType).stream().map(f -> new JpqlColumn<>(this, f)).collect(Collectors.toList());
        this.foreignPkMap = PersistenceUtils.getForeignFieldList(foreignType).stream()
                .collect(Collectors.toMap(field -> field, field -> PersistenceUtils.getPrimaryFieldList(field.getType())));
        this.relationField = PersistenceUtils.getRelationField(this.root.getEntityType(), foreignType);
        if (this.relationField == null) {
            throw new DeveloperMistakeException();
        }
    }


    @Override
    public String toQuery() {
        return String.format("%s.%s", this.root.getAlias(), this.relationField.getName());
    }

    @Override
    public void as(String alias) {
        this.alias = alias;
    }

    @Override
    public Column col(String fieldName, String alias) {
        JpqlColumn<R> target = getJpqlColumn(fieldName, alias);
        if ( target == null ) {
            throw new DMLValidationException(" entity type 에 없는 필드(컬럼)명입니다. ");
        }
        return target;
    }

    @Override
    public Column[] cols(String... fields) {
        int attrCount = fields.length;
        Column[] result = new Column[attrCount];
        for (int i=0; i<attrCount; i++) {
            result[i] = col(fields[i]);
        }
        return result;
    }

    @Override
    public Column[] all() {
        List<String> fieldNameList = PersistenceUtils.getJpqlColumns(this.foreignType);
        int fieldNameCount = fieldNameList.size();
        Column[] result = new Column[fieldNameCount];
        for (int i=0; i<fieldNameCount;i++) {
            result[i] = col(fieldNameList.get(i));
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
    public Class<R> getEntityType() {
        return this.foreignType;
    }

    @Override
    public boolean isJpql() {
        return true;
    }

    private JpqlColumn<R> getJpqlColumn(String fieldName, String alias) {
        boolean hasAlias = alias != null && !alias.isEmpty();
        JpqlColumn<R> target = null;
        int entityColumnCount = this.entityColumnList.size();
        for (int i=0; i<entityColumnCount ;i++) {
            JpqlColumn<R> jpqlColumn = this.entityColumnList.get(i);
            Field columnField = jpqlColumn.getField();
            if ( jpqlColumn.getAttr().equals(Strings.toCamel(fieldName)) ) {
                target = hasAlias
                        ? new JpqlColumn<>(this, columnField, alias)
                        : jpqlColumn;
                continue;
            }

            List<Field> foreignPrimaryFieldList = this.foreignPkMap.get(columnField);
            if ( foreignPrimaryFieldList != null ) {
                int foreignPrimaryKeyCount = foreignPrimaryFieldList.size();
                for (int j=0; j<foreignPrimaryKeyCount; j++) {
                    Field foreignPrimaryField = foreignPrimaryFieldList.get(j);
                    String foreignName = String.format("%s_%s", columnField.getName(), foreignPrimaryField.getName());
                    if (foreignName.equals(Strings.camel2Under(fieldName))) {
                        target = hasAlias
                                ? new JpqlColumn<>(this, columnField, foreignPrimaryField, alias)
                                : new JpqlColumn<>(this, columnField, foreignPrimaryField)
                        ;
                    }
                }
            }
        }

        return target;
    }
}
