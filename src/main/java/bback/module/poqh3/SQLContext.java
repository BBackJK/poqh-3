package bback.module.poqh3;

import java.util.List;

public interface SQLContext<T> extends OptionalExpression<T>, ResultProvider, SQL {


    SQLContext<T> select(Column... columns);

    From<T> from(Table<T> table);

    Class<T> getRootEntityType();

    List<Column> getSelectColumnList();

    boolean isJpql();
}
