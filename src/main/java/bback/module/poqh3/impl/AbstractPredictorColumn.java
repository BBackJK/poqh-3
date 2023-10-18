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
    public Predictor EQ(Column column) {
        return new Equals(this, column);
    }

    @Override
    public Predictor IN(Column... columns) {
        return new Ins(this, Arrays.asList(columns));
    }

    @Override
    public Predictor LIKE(Column column, LikeType likeType) {
        return new Likes(this, column, likeType);
    }

    @Override
    public Predictor NULL() {
        return new Nulls(this, false);
    }

    @Override
    public Predictor NOT_NULL() {
        return new Nulls(this, true);
    }
}
