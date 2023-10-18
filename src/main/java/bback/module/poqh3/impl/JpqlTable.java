package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.utils.PersistenceUtils;

import java.util.List;

public class JpqlTable implements Table {

    private final Class<?> entityType;

    private String alias;

    public JpqlTable(Class<?> entityType, String alias) {
        this.entityType = entityType;
        this.alias = alias;
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
    public boolean isJpql() {
        return true;
    }

    @Override
    public boolean isNative() {
        return false;
    }
}
