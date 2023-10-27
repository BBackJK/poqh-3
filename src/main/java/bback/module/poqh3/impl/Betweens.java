package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Predictor;

public class Betweens implements Predictor {

    private final Column source;

    private final Column from;
    private final Column to;

    private final boolean isNot;

    public Betweens(Column source, Column from, Column to) {
        this(source, from, to, false);
    }

    public Betweens(Column source, Column from, Column to, boolean isNot) {
        this.source = source;
        this.from = from;
        this.to = to;
        this.isNot = isNot;
    }


    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(source.toQuery());
        if ( isNot ) {
            sb.append(" not between ");
        } else {
            sb.append(" between ");
        }

        sb.append(from.toQuery());
        sb.append(" and ");
        sb.append(to.toQuery());

        return sb.toString();
    }
}
