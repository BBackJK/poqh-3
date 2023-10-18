package bback.module.poqh3.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class Objects {

    private Objects() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    public static boolean isEmpty(Object target) {
        if ( target == null ) {
            return true;
        }

        if ( target instanceof String ) {
            return ((String) target).isEmpty();
        }

        if ( target instanceof Collection ) {
            return ((Collection) target).isEmpty();
        }

        if ( target instanceof Map ) {
            return ((Map) target).isEmpty();
        }

        return false;
    }

    public static boolean orEmpty(Object... objs) {
        return Arrays.stream(objs).anyMatch(Objects::isEmpty);
    }

    public static boolean allEmpty(Object... objs) {
        return Arrays.stream(objs).allMatch(Objects::isEmpty);
    }
}
