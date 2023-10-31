package bback.module.poqh3.impl;


import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.utils.Strings;

class NativeColumn<T> extends AbstractPredictorColumn {

    private final Table<T> table;
    private final String field;
    private String alias;

    public NativeColumn(Table<T> table, String field) {
        this(table, field, null);
    }

    public NativeColumn(Table<T> table, String field, String alias) {
        this.table = table;
        this.field = field;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        return String.format("%s.%s", this.table.getAlias(), field);
    }

    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new NativeColumn<>(this.table, this.field, alias);
        }
        this.alias = alias;
        return this;
    }

    @Override
    public String getAttr() {
        return hasAlias() ? Strings.camel2Under(alias) : field;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }
}
