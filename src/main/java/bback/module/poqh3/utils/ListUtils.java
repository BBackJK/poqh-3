package bback.module.poqh3.utils;

import java.util.List;
import java.util.function.Supplier;

public final class ListUtils {

    private ListUtils() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    public static <T> T getOnSafety(List<T> list, int index) {
        return getOnSafety(list, index, () -> null);
    }

    public static <T> T getOnSafety(List<T> list, int index, Supplier<T> supplier) {
        try {
            return list.get(index);
        } catch (IndexOutOfBoundsException e) {
            if (supplier != null) {
                return supplier.get();
            }
            throw e;
        }
    }

}
