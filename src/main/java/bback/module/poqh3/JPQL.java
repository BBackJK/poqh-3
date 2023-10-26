package bback.module.poqh3;


import bback.module.poqh3.impl.JpqlTable;

public interface JPQL extends SQL {

    static <T> Table<T> TABLE(Class<T> entityType) {
        return TABLE(entityType, null);
    }

    static <T> Table<T> TABLE(Class<T> entityType, String alias) {
        return new JpqlTable<>(entityType, alias);
    }
}
