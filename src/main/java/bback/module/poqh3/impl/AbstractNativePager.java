package bback.module.poqh3.impl;

import bback.module.poqh3.Pager;

abstract class AbstractNativePager implements Pager {

    private int limit;
    private int offset;


    @Override
    public int getLimit() {
        return this.limit;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }


    static class NativeMysqlPager extends AbstractNativePager {

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

    // 12 버전 이상 부터 해당 쿼리 사용, 12 이전에는 rownum where 로 처리
    static class NativeOraclePager extends AbstractNativePager {
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

    static class NativeMssqlPager extends AbstractNativePager {
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
}
