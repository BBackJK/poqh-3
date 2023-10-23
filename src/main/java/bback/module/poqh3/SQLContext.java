package bback.module.poqh3;

import java.util.List;
import java.util.Optional;

public interface SQLContext<T> extends SQL {

    void SELECT(Column... columns);

    From FROM(Table table);

    void WHERE(Predictor... predictors);

    Order ORDER(Column column);

    void ORDER(Column... columns);

    void GROUP(Column... columns);

    List<T> toResultList();

    Optional<T> toResult();
}
