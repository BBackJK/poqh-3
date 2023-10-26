package bback.module.poqh3;

public interface Order extends Native, JPQL {

    void desc();
    void asc();


    enum OrderBy {
        ASC, DESC
    }
}
