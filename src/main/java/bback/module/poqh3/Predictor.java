package bback.module.poqh3;


import bback.module.poqh3.impl.And;
import bback.module.poqh3.impl.Or;

public interface Predictor extends SQL {

    default And and(Predictor predictor) {
        return new And(this, predictor);
    }

    default Or or(Predictor predictor) {
        return new Or(this, predictor);
    }

    default boolean isConnector() {
        return false;
    }
}
