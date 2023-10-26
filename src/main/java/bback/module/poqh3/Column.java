package bback.module.poqh3;

import bback.module.poqh3.impl.Value;

import java.util.Arrays;
import java.util.List;

public interface Column extends Native, JPQL {

    String getAttr();
    String getAlias();
    boolean hasAlias();

    Predictor eq(Column column);

    Predictor gt(Column column);

    Predictor ge(Column column);

    Predictor lt(Column column);

    Predictor le(Column column);

    Predictor in(Column... columns);

    Predictor like(Column column, LikeType likeType);

    Predictor isNull();

    Predictor isNotNull();

    default Predictor like(Column column) {
        return like(column, LikeType.ANY);
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
