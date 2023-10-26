package bback.module.poqh3;


import jakarta.persistence.criteria.JoinType;

public interface From<T> extends Native, JPQL {

    <R> Join<T,R> JOIN(Table<R> joinTable, JoinType joinType);

    default <R> Join<T,R> JOIN(Table<R> joinTable) {
        return JOIN(joinTable, JoinType.INNER);
    }

    Table<T> getRoot();

    boolean isJpql();
}
