package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.TableIsOnlyAcceptEntityException;
import bback.module.poqh3.utils.PersistenceUtils;
import bback.module.poqh3.utils.Strings;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpqlTable<T> implements Table<T> {

    private final Class<T> entityType;

    private final List<JpqlColumn<T>> entityColumnList;

    private final Map<Field, List<Field>> foreignPkMap;

    private String alias;

    public JpqlTable(Class<T> entityType, String alias) {
        if (!PersistenceUtils.isEntityClass(entityType)) {
            throw new TableIsOnlyAcceptEntityException();
        }
        this.entityType = entityType;
        this.alias = alias;
        this.entityColumnList = PersistenceUtils.getColumnFields(entityType).stream().map(f -> new JpqlColumn<>(this, f)).collect(Collectors.toList());
        this.foreignPkMap = PersistenceUtils.getForeignFieldList(entityType).stream()
                .collect(Collectors.toMap(field -> field, field -> PersistenceUtils.getPrimaryFieldList(field.getType())));
    }


    @Override
    public String toQuery() {
        return this.entityType.getSimpleName();
    }

    @Override
    public void as(String alias) {
        this.alias = alias;
    }

    @Override
    public Column col(String fieldName, String alias) {
        JpqlColumn<T> target = getJpqlColumn(fieldName, alias);
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
        List<String> fieldNameList = PersistenceUtils.getJpqlColumns(this.entityType);
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
    public Class<T> getEntityType() {
        return this.entityType;
    }

    @Override
    public boolean isJpql() {
        return true;
    }

    private JpqlColumn<T> getJpqlColumn(String fieldName, String alias) {
        boolean hasAlias = alias != null && !alias.isEmpty();
        JpqlColumn<T> target = null;
        int entityColumnCount = this.entityColumnList.size();
        for (int i=0; i<entityColumnCount ;i++) {
            JpqlColumn<T> jpqlColumn = this.entityColumnList.get(i);
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
