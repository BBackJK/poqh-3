package bback.module.poqh3.impl;

import bback.module.poqh3.Column;

public class Value extends AbstractPredictorColumn {

    private final Object data;
    private String alias;

    public Value(Object data) {
        this(data, null);
    }

    public Value(Object data, String alias) {
        this.data = data;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        if (data == null) {
            return null;
        }
        if (data instanceof String) {
            return "'" + this.data + "'";
        }
        return String.format("%s", this.data);
    }

    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new Value(this.data, alias);
        }
        this.alias = alias;
        return this;
    }

    @Override
    public String getAttr() {
        return hasAlias() ? this.alias : this.data.toString();
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
