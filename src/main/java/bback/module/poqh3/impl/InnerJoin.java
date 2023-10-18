package bback.module.poqh3.impl;

import bback.module.poqh3.From;
import bback.module.poqh3.Table;

class InnerJoin extends AbstractJoin {


    public InnerJoin(From from, Table joinTable) {
        super(from, joinTable);
    }

    @Override
    protected String getExpression() {
        return " join ";
    }
}
