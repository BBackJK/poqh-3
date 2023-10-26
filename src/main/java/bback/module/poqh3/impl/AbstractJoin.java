package bback.module.poqh3.impl;

import bback.module.poqh3.*;
import bback.module.poqh3.exceptions.DMLValidationException;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

abstract class AbstractJoin<T,R> implements Join<T,R> {

    protected final SQLContext<T> context;

    protected final From<T> from;
    protected final Table<R> joinTable;
    protected final List<Predictor> onList = new ArrayList<>();

    protected AbstractJoin(SQLContext<T> context, From<T> from, Table<R> joinTable) {
        this.context = context;
        this.from = from;
        this.joinTable = joinTable;
    }

    protected abstract String getExpression();

    @Override
    public From<T> on(Predictor... predictors) {
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

    @Override
    public SQLContext<T> where(Predictor... predictors) {
        return this.context.where(predictors);
    }

    @Override
    public SQLContext<T> order(Column... columns) {
        return this.context.order(columns);
    }

    @Override
    public SQLContext<T> order(Column column, Order.OrderBy orderBy) {
        return this.context.order(column, orderBy);
    }

    @Override
    public SQLContext<T> group(Column... columns) {
        return this.context.group(columns);
    }

    @Override
    public <R> List<R> toResultList(Class<R> resultType) throws PersistenceException {
        return this.context.toResultList(resultType);
    }

    @Override
    public <R> Optional<R> toResult(Class<R> resultType) throws PersistenceException {
        return this.context.toResult(resultType);
    }
}
