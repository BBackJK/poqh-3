package bback.module.poqh3;

import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.Optional;

public interface SQLContext<T> extends SQL {

    void SELECT(Column... columns);

    From<T> FROM(Table<T> table);

    void WHERE(Predictor... predictors);

    Order ORDER(Column column);

    void ORDER(Column... columns);

    void GROUP(Column... columns);

//    SQLContext select(Column... columns);
//
//    SQLContext from(Table<?> table);



    <R> List<R> toResultList(Class<R> resultType) throws PersistenceException;

    <R> Optional<R> toResult(Class<R> resultType) throws PersistenceException;

    Class<T> getRootEntityType();

    List<Column> getSelectColumnList();

    boolean isJpql();
}
