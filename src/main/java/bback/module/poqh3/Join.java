package bback.module.poqh3;

public interface Join<T,R> extends OptionalExpression<T>, PaginationExpression<T>, ResultProvider, SQL {


    From<T> on(Predictor... predictors);

    Table<R> getJoinTable();
}
