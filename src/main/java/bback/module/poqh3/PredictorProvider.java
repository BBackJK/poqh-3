package bback.module.poqh3;

public interface PredictorProvider {

    Predictor eq(Column column);

    Predictor neq(Column column);

    Predictor gt(Column column);

    Predictor ge(Column column);

    Predictor lt(Column column);

    Predictor le(Column column);

    Predictor in(Column... columns);

    Predictor notIn(Column... columns);

    Predictor like(Column column, LikeType likeType);

    Predictor isNull();

    Predictor isNotNull();

    Predictor between(Column from, Column to);

    Predictor notBetween(Column from, Column to);

    default Predictor like(Column column) {
        return like(column, LikeType.ANY);
    }
}
