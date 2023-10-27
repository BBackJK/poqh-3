package bback.module.poqh3.impl;


import bback.module.poqh3.Column;
import bback.module.poqh3.LikeType;
import bback.module.poqh3.Predictor;

import java.util.Arrays;

abstract class AbstractPredictorColumn implements Column {

    @Override
    public Predictor eq(Column column) {
        return new Equals(this, column);
    }

    @Override
    public Predictor neq(Column column) {
        return new Equals(this, column, true);
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
    public Predictor notIn(Column... columns) {
        return new Ins(this, Arrays.asList(columns), true);
    }

    @Override
    public Predictor like(Column column, LikeType likeType) {
        return new Likes(this, column, likeType);
    }

    @Override
    public Predictor isNull() {
        return new Nulls(this);
    }

    @Override
    public Predictor isNotNull() {
        return new Nulls(this, true);
    }

    @Override
    public Predictor between(Column from, Column to) {
        return new Betweens(this, from, to);
    }

    @Override
    public Predictor notBetween(Column from, Column to) {
        return new Betweens(this, from, to, true);
    }
}
