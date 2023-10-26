package bback.module.poqh3;

public interface OptionalExpression<T> {

    SQLContext<T> where(Predictor... predictors);

    SQLContext<T> order(Column... columns);

    SQLContext<T> order(Column column, Order.OrderBy orderBy);

    SQLContext<T> group(Column... columns);

    default SQLContext<T> order(Column column) {
        return order(column, Order.OrderBy.ASC);
    }
}
