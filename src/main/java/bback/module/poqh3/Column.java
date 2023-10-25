package bback.module.poqh3;

import bback.module.poqh3.impl.Value;

public interface Column extends Native, JPQL {

    String getAttr();
    boolean hasAlias();

    Predictor EQ(Column column);

    Predictor GT(Column column);

    Predictor GE(Column column);

    Predictor LT(Column column);

    Predictor LE(Column column);

    Predictor IN(Column... columns);

    Predictor LIKE(Column column, LikeType likeType);

    Predictor NULL();
    Predictor NOT_NULL();

    default Predictor LIKE(Column column) {
        return LIKE(column, LikeType.ANY);
    }

    static Value VALUE(Object data) {
        return new Value(data);
    }
}
