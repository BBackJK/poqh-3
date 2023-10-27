package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.TableIsOnlyAcceptEntityException;
import bback.module.poqh3.utils.PersistenceUtils;
import bback.module.poqh3.utils.Strings;

import java.util.List;
import java.util.stream.Collectors;

public class NativeTable<T> implements Table<T> {

    private final Class<T> entityType;
    private final List<NativeColumn<T>> entityColumnList;
    private final String tableName;
    private String alias;

    public NativeTable(Class<T> entityType, String alias) {
        if (!PersistenceUtils.isEntityClass(entityType)) {
            throw new TableIsOnlyAcceptEntityException();
        }
        this.entityType = entityType;
        this.tableName = PersistenceUtils.getTableName(entityType);
        this.entityColumnList = PersistenceUtils.getNativeColumns(entityType).stream().map(nativeColumnName -> new NativeColumn<>(this, nativeColumnName)).collect(Collectors.toList());
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        return this.tableName;
    }

    @Override
    public void as(String alias) {
        this.alias = alias;
    }

    @Override
    public Column col(String field, String alias) {
        NativeColumn<T> target = this.getNativeColumn(field, alias);
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
        List<String> fieldNameList = PersistenceUtils.getNativeColumns(this.entityType);
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
        return false;
    }

    private NativeColumn<T> getNativeColumn(String fieldName, String alias) {
        boolean hasAlias = alias != null && !alias.isEmpty();
        String underFieldName = Strings.camel2Under(fieldName);
        int entityColumnCount = this.entityColumnList.size();
        for (int i=0; i<entityColumnCount;i++) {
            NativeColumn<T> nativeColumn = this.entityColumnList.get(i);
            if ( nativeColumn.getAttr().equals(underFieldName) ) {
                return hasAlias
                        ? new NativeColumn<>(this, underFieldName, alias)
                        : nativeColumn;
            }
        }
        return null;
    }
}
