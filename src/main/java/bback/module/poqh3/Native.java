package bback.module.poqh3;

import bback.module.poqh3.impl.ContextTable;
import bback.module.poqh3.impl.NativeTable;

public interface Native extends SQL {

    static <T> Table<T> TABLE(Class<T> clazz) {
        return TABLE(clazz, null);
    }

    static <T> Table<T> TABLE(Class<T> clazz, String alias) {
        return new NativeTable(clazz, alias);
    }

//    static <T> Table<T> TABLE(SQLContext<T> context, String alias) {
//        return new ContextTable<>(context, alias);
//    }

    static <T, E extends SQLContext<T>> Table<E> TABLE(E context, String alias) {
        return new ContextTable<>(context, alias);
    }
}
