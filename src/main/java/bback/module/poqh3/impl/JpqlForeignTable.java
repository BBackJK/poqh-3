package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DeveloperMistakeException;
import bback.module.poqh3.utils.PersistenceUtils;

import java.lang.reflect.Field;
import java.util.List;

public class JpqlForeignTable implements Table {

    private final Table root;
    private final Class<?> foreignType;
    private final Field relationField;
    private String alias;

    public JpqlForeignTable(Table root, Class<?> foreignType, String alias) {
        this.root = root;
        this.foreignType = foreignType;
        this.alias = alias;
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
        List<String> fieldNameList = PersistenceUtils.getJpqlColumns(this.foreignType);
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
        return this.foreignType;
    }

    @Override
    public boolean isJpql() {
        return true;
    }
}
