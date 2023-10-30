package bback.module.poqh3.utils;

public final class Strings {

    public static final String EMPTY = "";

    private Strings() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    public static String camel2Under(String target) {
        StringBuilder result = new StringBuilder();
        if (target == null || target.isEmpty()) {
            return result.toString();
        }

        result.append(Character.toLowerCase(target.charAt(0)));
        int charCount = target.length();
        for (int i=1; i<charCount; i++) {
            char c = target.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
        }

        return result.toString();
    }

    public static String under2Camel(String target) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (target != null && target.length() > 0) {
            if (target.length() > 1 && target.charAt(1) == '_') {
                result.append(Character.toUpperCase(target.charAt(0)));
            }
            else {
                result.append(Character.toLowerCase(target.charAt(0)));
            }
            for (int i = 1; i < target.length(); i++) {
                char c = target.charAt(i);
                if (c == '_') {
                    nextIsUpper = true;
                }
                else {
                    if (nextIsUpper) {
                        result.append(Character.toUpperCase(c));
                        nextIsUpper = false;
                    }
                    else {
                        result.append(Character.toLowerCase(c));
                    }
                }
            }
        }
        return result.toString();
    }

    public static String toCamel(String src) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        int length = src.length();
        boolean isPrevUnderBar = false;

        char firstChar = src.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            result.append(Character.toLowerCase(firstChar));
        } else {
            result.append(firstChar);
        }

        for (int i=1; i<length; i++) {
            char c = src.charAt(i);
            if ( c == '_' ) {
                isPrevUnderBar = true;
                continue;
            }

            if (isPrevUnderBar) {
                result.append(Character.toUpperCase(c));
                isPrevUnderBar = false;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}
