package bback.module.poqh3;

public interface Order extends Native, JPQL {

    void DESC();
    void ASC();


    enum OrderBy {
        ASC, DESC
    }
}
