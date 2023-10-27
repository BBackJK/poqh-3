package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Predictor;

class Equals implements Predictor {

    private final Column source;

    private final Column target;

    private final boolean isNot;

    public Equals(Column source, Column target) {
        this(source, target, false);
    }

    public Equals(Column source, Column target, boolean isNot) {
        this.source = source;
        this.target = target;
        this.isNot = isNot;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.toQuery());
        sb.append(
                isNot ? " != " : " = "
        );
        sb.append(target.toQuery());
        return sb.toString();
    }
}
