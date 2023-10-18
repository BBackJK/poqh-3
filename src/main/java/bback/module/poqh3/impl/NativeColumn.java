package bback.module.poqh3.impl;


import bback.module.poqh3.Table;
import bback.module.poqh3.utils.Strings;

class NativeColumn extends AbstractPredictorColumn {

    private final Table table;
    private final String field;
    private String alias;

    public NativeColumn(Table table, String field, String alias) {
        this.table = table;
        this.field = field;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        return String.format("%s.%s", this.table.getAlias(), Strings.camel2Under(field));
    }

    @Override
    public String getAttr() {
        return hasAlias() ? Strings.camel2Under(alias) : Strings.camel2Under(field);
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }
}
