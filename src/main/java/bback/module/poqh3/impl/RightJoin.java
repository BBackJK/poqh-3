package bback.module.poqh3.impl;

import bback.module.poqh3.From;
import bback.module.poqh3.SQLContext;
import bback.module.poqh3.Table;

class RightJoin<T, R> extends AbstractJoin<T, R> {

    public RightJoin(SQLContext<T> context, From<T> from, Table<R> joinTable) {
        super(context, from, joinTable);
    }

    @Override
    protected String getExpression() {
        return " right join ";
    }
}
