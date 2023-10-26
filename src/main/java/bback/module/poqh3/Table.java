package bback.module.poqh3;

public interface Table<T> extends JPQL, Native {

    void AS(String alias);

    void AS(int tableIndex);

    default Column COLUMN(String field) {
        return COLUMN(field, null);
    }

    Column COLUMN(String field, String alias);
    Column[] COLUMNS(String... fields);

    Column[] ALL();

    boolean hasAlias();

    String getAlias();

    Class<T> getEntityType();

    boolean isJpql();

}
