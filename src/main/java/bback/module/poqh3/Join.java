package bback.module.poqh3;

public interface Join extends SQL {

    From ON(Predictor... predictors);

    Table getJoinTable();
}
