package bback.module.poqh3.utils;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ClassUtils {

    private static final Map<Class<?>, Object> PRIMITIVE_INIT_VALUE_MAP = new IdentityHashMap<>(9);

    private ClassUtils() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    static {
        PRIMITIVE_INIT_VALUE_MAP.put(boolean.class, false);
        PRIMITIVE_INIT_VALUE_MAP.put(byte.class, (byte)0);
        PRIMITIVE_INIT_VALUE_MAP.put(char.class, '\u0000');
        PRIMITIVE_INIT_VALUE_MAP.put(double.class, 0.0d);
        PRIMITIVE_INIT_VALUE_MAP.put(float.class, 0.0);
        PRIMITIVE_INIT_VALUE_MAP.put(int.class, 0);
        PRIMITIVE_INIT_VALUE_MAP.put(long.class, 0L);
        PRIMITIVE_INIT_VALUE_MAP.put(short.class, (short)0);
        PRIMITIVE_INIT_VALUE_MAP.put(void.class, null);
    }

    public static Object getTypeInitValue(Class<?> clazz) {
        if (!clazz.isPrimitive()) return null;
        return PRIMITIVE_INIT_VALUE_MAP.get(clazz);
    }

    public static boolean isMapType(Class<?> clazz) {
        return clazz != null &&
                (
                        Arrays.asList(clazz.getInterfaces()).contains(Map.class)
                        || clazz.isAssignableFrom(Map.class)
                );
    }

    public static boolean isListType(Class<?> clazz) {
        return clazz != null &&
                (
                        Arrays.asList(clazz.getInterfaces()).contains(List.class)
                                || clazz.isAssignableFrom(List.class)
                );
    }
}
