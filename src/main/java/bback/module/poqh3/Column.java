package bback.module.poqh3;

import bback.module.poqh3.impl.Value;

import java.util.Arrays;
import java.util.List;

public interface Column extends PredictorProvider, Native, JPQL {

    String getAttr();
    String getAlias();
    boolean hasAlias();

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
