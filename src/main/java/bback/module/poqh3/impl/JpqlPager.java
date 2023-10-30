package bback.module.poqh3.impl;

import bback.module.poqh3.Pager;
import bback.module.poqh3.utils.Strings;

public class JpqlPager implements Pager {

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

    @Override
    public String toQuery() {
        return Strings.EMPTY;
    }
}
