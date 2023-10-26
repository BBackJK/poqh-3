package bback.module.poqh3.impl;

import bback.module.poqh3.From;
import bback.module.poqh3.Join;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.utils.Objects;
import bback.module.poqh3.utils.PersistenceUtils;
import jakarta.persistence.criteria.JoinType;

import java.util.ArrayList;
import java.util.List;

class FromImpl<T> implements From<T> {

    private final Table<T> root;
    private final List<Join<T, ?>> joinList = new ArrayList<>();

    public FromImpl(Table<T> root) {
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
    public <R> Join<T,R> JOIN(Table<R> joinTable, JoinType joinType) {
        this.validationTable(joinTable, joinType);

        if ( !joinTable.hasAlias() ) {
            int joinCount = this.joinList.size();
            joinTable.AS(joinCount + 2);
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
                join = new LeftJoin<>(this, joinTable);
                break;
            case RIGHT:
                join = new RightJoin<>(this, joinTable);
                break;
            default:
                join = new InnerJoin<>(this, joinTable);
                break;
        }
        this.joinList.add(join);
        return join;
    }

    @Override
    public Table<T> getRoot() {
        return this.root;
    }

    @Override
    public boolean isJpql() {
        return this.root.isJpql();
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
