package bback.module.poqh3.impl;

class NativePostgrePager extends AbstractNativePager {

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" offset ");
        sb.append(getOffset());
        sb.append(" rows fetch next ");
        sb.append(getLimit());
        sb.append(" rows only ");
        return sb.toString();
    }
}
