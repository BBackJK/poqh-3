package bback.module.poqh3.impl;

import bback.module.poqh3.From;
import bback.module.poqh3.Join;
import bback.module.poqh3.Predictor;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractJoin<T,R> implements Join<T,R> {

    protected final From<T> from;
    protected final Table<R> joinTable;
    protected final List<Predictor> onList = new ArrayList<>();

    protected AbstractJoin(From<T> from, Table<R> joinTable) {
        this.from = from;
        this.joinTable = joinTable;
    }

    protected abstract String getExpression();

    @Override
    public From<T> ON(Predictor... predictors) {
        this.onList.addAll(Arrays.stream(predictors).collect(Collectors.toList()));
        return this.from;
    }

    @Override
    public Table<R> getJoinTable() {
        return this.joinTable;
    }

    @Override
    public String toQuery() {
        this.validationQuery();
        StringBuilder sb = new StringBuilder();
        sb.append(this.getExpression());
        sb.append(this.joinTable.toQuery());
        sb.append(" ");
        sb.append(this.joinTable.getAlias());

        int onCount = this.onList.size();
        if ( onCount > 0 ) {
            sb.append("\n\ton ");
        }
        for ( int i=0; i<onCount; i++ ) {
            int n = i+1;
            boolean isLast = n == onCount;
            Predictor predictor = this.onList.get(i);
            sb.append(predictor.toQuery());
            if (!isLast) sb.append("\n\t and ");
        }
        return sb.toString();
    }

    private void validationQuery() {
        if ( this.from == null || this.joinTable == null ) {
            throw new DMLValidationException(" join table empty  ");
        }
    }
}
