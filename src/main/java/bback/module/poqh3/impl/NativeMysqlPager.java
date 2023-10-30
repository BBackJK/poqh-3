package bback.module.poqh3.impl;

class NativeMysqlPager extends AbstractNativePager {

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        if ( getLimit() > 0 ) {
            sb.append(" ");
            sb.append("limit");
            sb.append(" ");
            sb.append(getLimit());
        }
        if (getOffset() > 0) {
            sb.append(" ");
            sb.append("offset");
            sb.append(" ");
            sb.append(getOffset());
        }
        return sb.toString();
    }
}
