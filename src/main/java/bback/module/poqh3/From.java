package bback.module.poqh3;


import jakarta.persistence.criteria.JoinType;

public interface From<T> extends OptionalExpression<T>, ResultProvider, Native, JPQL {


    <R> Join<T,R> join(Table<R> joinTable, JoinType joinType);

    SQLContext<T> where(Predictor... predictors);

    Table<T> getRoot();

    boolean isJpql();


    default <R> Join<T,R> join(Table<R> joinTable) {
        return join(joinTable, JoinType.INNER);
    }
}
