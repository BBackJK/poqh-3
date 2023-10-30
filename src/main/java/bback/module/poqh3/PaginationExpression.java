package bback.module.poqh3;

public interface PaginationExpression<T> {

    SQLContext<T> limit(int limit);

    SQLContext<T> offset(int offset);
}
