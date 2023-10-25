package bback.module.poqh3;

import bback.module.poqh3.impl.ContextTable;
import bback.module.poqh3.impl.NativeTable;

public interface Native extends SQL {

    static Table TABLE(Class<?> clazz) {
        return TABLE(clazz, null);
    }

    static Table TABLE(Class<?> clazz, String alias) {
        return new NativeTable(clazz, alias);
    }

    static Table TABLE(SQLContext<?> context, String alias) {
        return new ContextTable(context, alias);
    }
}
