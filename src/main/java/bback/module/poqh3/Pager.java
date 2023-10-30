package bback.module.poqh3;

public interface Pager extends JPQL, Native {

    int getLimit();

    int getOffset();

    void setLimit(int limit);

    void setOffset(int offset);
}
