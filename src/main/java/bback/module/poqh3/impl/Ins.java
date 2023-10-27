package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Predictor;

import java.util.List;

class Ins implements Predictor {

    private final Column source;

    private final List<Column> inList;

    private final boolean isNot;


    public Ins(Column source, List<Column> inList) {
        this(source, inList, false);
    }

    public Ins(Column source, List<Column> inList, boolean isNot) {
        this.source = source;
        this.inList = inList;
        this.isNot = isNot;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(source.toQuery());
        if ( isNot ) {
            sb.append(" not ");
        }
        sb.append(" in ");
        int inCount = this.inList.size();
        for (int i=0; i<inCount; i++) {
            int n = i+1;
            boolean isFirst = n == 1;
            boolean isLast = n == inCount;

            if (isFirst) sb.append("( ");

            Column inColumn = this.inList.get(i);
            sb.append(inColumn.toQuery());
            if (!isLast) sb.append(", ");
            else sb.append(" )");
        }
        return sb.toString();
    }
}
