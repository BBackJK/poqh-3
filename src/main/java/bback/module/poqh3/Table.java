package bback.module.poqh3;

public interface Table<T> extends JPQL, Native {

    void as(String alias);

    default void as(int tableIndex) {
        as(String.format("t%d", tableIndex));
    }

    default Column col(String field) {
        return col(field, null);
    }

    Column col(String field, String alias);
    Column[] cols(String... fields);

    Column[] all();

    boolean hasAlias();

    String getAlias();

    Class<T> getEntityType();

    boolean isJpql();

}
