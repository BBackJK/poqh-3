package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.utils.Strings;

import java.lang.reflect.Field;

class JpqlColumn<T> extends AbstractPredictorColumn {

    private final Table<T> table;
    private final Field field; // table entity class 에 대한 field
    private final Field foreignField;
    private String alias;

    public JpqlColumn(Table<T> table, Field field) {
        this(table, field, (String) null);
    }

    public JpqlColumn(Table<T> table, Field field, Field foreignField) {
        this(table, field, foreignField, null);
    }

    public JpqlColumn(Table<T> table, Field field, String alias) {
        this(table, field, null, alias);
    }

    public JpqlColumn(Table<T> table, Field field, Field foreignField, String alias) {
        this.table = table;
        this.field = field;
        this.foreignField = foreignField;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.table.getAlias());
        sb.append(".");
        sb.append(field.getName());
        if ( this.hasForeignField() ) {
            sb.append(".");
            sb.append(foreignField.getName());
        }
        return sb.toString();
    }

    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new JpqlColumn<>(this.table, this.field, this.foreignField, alias);
        }
        this.alias = alias;
        return this;
    }

    @Override
    public String getAttr() {
        return hasAlias() ? getAlias() : this.field.getName();
    }

    @Override
    public String getAlias() {
        return Strings.toCamel(this.alias);
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }

    @Override
    public boolean isJpqlColumn() {
        return true;
    }

    public Field getField() {
        return this.field;
    }

    private boolean hasForeignField() {
        return this.foreignField != null;
    }
}
