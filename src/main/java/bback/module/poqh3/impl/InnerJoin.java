package bback.module.poqh3.impl;

import bback.module.poqh3.From;
import bback.module.poqh3.Table;

class InnerJoin<T, R> extends AbstractJoin<T, R> {


    public InnerJoin(From<T> from, Table<R> joinTable) {
        super(from, joinTable);
    }

    @Override
    protected String getExpression() {
        return " join ";
    }
}
