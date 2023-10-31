package bback.module.poqh3.impl;

import bback.module.poqh3.Column;

class NullColumn extends AbstractPredictorColumn {

    @Override
    public String toQuery() {
        return "null";
    }

    @Override
    public Column as(String alias) {
        return this;
    }

    @Override
    public String getAttr() {
        return "";
    }

    @Override
    public String getAlias() {
        return "";
    }

    @Override
    public boolean hasAlias() {
        return false;
    }

    @Override
    public boolean isNullColumn() {
        return true;
    }
}
