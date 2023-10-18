package bback.module.poqh3;

import java.util.List;
import java.util.Optional;

public interface SQLContext<T> extends SQL {

    From FROM(Table table);

    void SELECT(Column... columns);

    List<T> toResultList();

    Optional<T> toResult();

    void WHERE(Predictor... predictors);
}
