package bback.module.poqh3;

public interface Join<T,R> extends SQL {

    From<T> ON(Predictor... predictors);

    Table<R> getJoinTable();
}
