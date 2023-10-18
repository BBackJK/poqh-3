package bback.module.poqh3.impl;


import bback.module.poqh3.From;
import bback.module.poqh3.Table;

class LeftJoin extends AbstractJoin {

    public LeftJoin(From from, Table joinTable) {
        super(from, joinTable);
    }

    @Override
    protected String getExpression() {
        return " left join ";
    }
}
