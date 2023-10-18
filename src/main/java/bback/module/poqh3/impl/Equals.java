package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Predictor;

class Equals implements Predictor {

    private final Column source;

    private final Column target;

    public Equals(Column source, Column target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toQuery() {
        return String.format("%s = %s", source.toQuery(), target.toQuery());
    }
}
