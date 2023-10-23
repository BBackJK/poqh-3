package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Order;

class OrderImpl implements Order {

    private final Column column;
    private OrderBy orderBy;

    public OrderImpl(Column column) {
        this(column, OrderBy.ASC);
    }

    public OrderImpl(Column column, OrderBy orderBy) {
        this.column = column;
        this.orderBy = orderBy;
    }


    @Override
    public void DESC() {
        this.orderBy = OrderBy.DESC;
    }

    @Override
    public void ASC() {
        this.orderBy = OrderBy.ASC;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(this.column.toQuery());
        if (!isAsc()) {
            sb.append(" desc");
        }
        return sb.toString();
    }

    private boolean isAsc() {
        return OrderBy.ASC.equals(this.orderBy);
    }
}
