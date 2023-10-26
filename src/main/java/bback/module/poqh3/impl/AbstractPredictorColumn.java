package bback.module.poqh3.impl;


import bback.module.poqh3.Column;
import bback.module.poqh3.LikeType;
import bback.module.poqh3.Predictor;

import java.util.Arrays;

abstract class AbstractPredictorColumn implements Column {

    public abstract String getAttr();

    public abstract boolean hasAlias();

    public abstract String toQuery();


    @Override
    public Predictor eq(Column column) {
        return new Equals(this, column);
    }

    @Override
    public Predictor gt(Column column) {
        return new Greaters(this, column);
    }

    @Override
    public Predictor ge(Column column) {
        return new Greaters(this, column, true);
    }

    @Override
    public Predictor lt(Column column) {
        return new Lesses(this, column);
    }

    @Override
    public Predictor le(Column column) {
        return new Lesses(this, column, true);
    }

    @Override
    public Predictor in(Column... columns) {
        return new Ins(this, Arrays.asList(columns));
    }

    @Override
    public Predictor like(Column column, LikeType likeType) {
        return new Likes(this, column, likeType);
    }

    @Override
    public Predictor isNull() {
        return new Nulls(this, false);
    }

    @Override
    public Predictor isNotNull() {
        return new Nulls(this, true);
    }
}
