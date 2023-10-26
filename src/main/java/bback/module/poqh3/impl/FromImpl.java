package bback.module.poqh3.impl;

import bback.module.poqh3.*;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.utils.Objects;
import bback.module.poqh3.utils.PersistenceUtils;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.JoinType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class FromImpl<T> implements From<T> {

    private final SQLContext<T> context;
    private final Table<T> root;
    private final List<Join<T, ?>> joinList = new ArrayList<>();

    public FromImpl(SQLContext<T> context, Table<T> root) {
        this.context = context;
        this.root = root;
    }


    @Override
    public String toQuery() {
        if (root == null) {
            throw new DMLValidationException(" root table is null ");
        }
        StringBuilder sb = new StringBuilder(" from ");
        sb.append(this.root.toQuery());
        sb.append(" ");
        sb.append(this.root.getAlias());

        if (!this.joinList.isEmpty()) {
            sb.append("\n");
            this.joinList.forEach(j -> {
                sb.append(j.toQuery());
                sb.append("\n");
            });
        }

        return sb.toString();
    }

    @Override
    public <R> Join<T, R> join(Table<R> joinTable, JoinType joinType) {
        this.validationTable(joinTable, joinType);

        if ( !joinTable.hasAlias() ) {
            int joinCount = this.joinList.size();
            joinTable.as(joinCount + 2);
        }

        if (this.isJpql()) {
            boolean isRelation = PersistenceUtils.isRelationEntity(this.root.getEntityType(), joinTable.getEntityType());
            if (isRelation) {
                joinTable = new JpqlForeignTable<>(this.root, joinTable.getEntityType(), joinTable.getAlias());
            }
        }

        Join<T,R> join = null;
        switch (joinType) {
            case LEFT:
                join = new LeftJoin<>(this.context, this, joinTable);
                break;
            case RIGHT:
                join = new RightJoin<>(this.context, this, joinTable);
                break;
            default:
                join = new InnerJoin<>(this.context, this, joinTable);
                break;
        }
        this.joinList.add(join);
        return join;
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
    public Table<T> getRoot() {
        return this.root;
    }

    @Override
    public boolean isJpql() {
        return this.root.isJpql();
    }

    @Override
    public <R> List<R> toResultList(Class<R> resultType) throws PersistenceException {
        return this.context.toResultList(resultType);
    }

    @Override
    public <R> Optional<R> toResult(Class<R> resultType) throws PersistenceException {
        return this.context.toResult(resultType);
    }

    private void validationTable(Table<?> joinTable, JoinType joinType) {
        if ( Objects.orEmpty( joinTable, joinType ) ) {
            throw new DMLValidationException("join table or join type is null.");
        }
        if (this.isJpql() && !joinTable.isJpql()) {
            throw new DMLValidationException("JPQL Table is only use JPQL Table.");
        }

        if (!this.isJpql() && joinTable.isJpql()) {
            throw new DMLValidationException("Native Table is only use Native Table.");
        }
    }
}
