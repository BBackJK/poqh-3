package bback.module.poqh3.utils;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ClassUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_CLASS_MAP = new IdentityHashMap<>(9);
    private static final Map<Class<?>, Object> PRIMITIVE_INIT_VALUE_MAP = new IdentityHashMap<>(9);

    private ClassUtils() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    static {
        PRIMITIVE_WRAPPER_CLASS_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(char.class, Character.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(double.class, Double.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(float.class, Float.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(int.class, Integer.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(long.class, Long.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_CLASS_MAP.put(void.class, Void.class);

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

    public static boolean isReference(Class<?> clazz) {
        if ( clazz == null ) {
            return false;
        }

        if (isPrimitiveOrWrapper(clazz)) {
            return false;
        }

        if (isListType(clazz) || isMapType(clazz)) {
            return false;
        }

        return true;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clz) {
        return clz != null && (clz.isPrimitive() || PRIMITIVE_WRAPPER_CLASS_MAP.values().stream().anyMatch(clz::equals));
    }
}
