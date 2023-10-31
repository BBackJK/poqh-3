package bback.module.poqh3;

import bback.module.poqh3.impl.Value;

import java.util.Arrays;
import java.util.List;

public interface Column extends PredictorProvider, Native, JPQL {

    Column as(String alias);

    String getAttr();
    String getAlias();
    boolean hasAlias();

    default boolean isNullColumn() {
        return false;
    }

    default boolean isJpqlColumn() {
        return false;
    }

    default boolean isFunctional() {
        return false;
    }

    static Value VALUE(Object data) {
        return new Value(data);
    }

    static <T> Value[] VALUES(T... data) {
        return VALUES(Arrays.asList(data));
    }

    static <T> Value[] VALUES(List<T> dataList) {
        int count = dataList.size();
        Value[] values = new Value[count];
        for (int i=0; i<count;i++) {
            values[i] = new Value(dataList.get(i));
        }
        return values;
    }
}
