package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Predictor;

class Greaters implements Predictor {

    private final Column source;
    private final Column target;
    private final boolean isEquals;

    public Greaters(Column source, Column target) {
        this(source, target, false);
    }

    public Greaters(Column source, Column target, boolean isEquals) {
        this.source = source;
        this.target = target;
        this.isEquals = isEquals;
    }


    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(source.toQuery());
        sb.append(" ");
        sb.append(">");
        if (isEquals) {
            sb.append("=");
        }
        sb.append(" ");
        sb.append(target.toQuery());
        return sb.toString();
    }
}
