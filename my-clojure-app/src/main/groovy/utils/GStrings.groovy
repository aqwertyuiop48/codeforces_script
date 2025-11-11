package utils

class GStrings {
    static String reverse(String s) {
        return s.reverse()
    }

    static String capitalize(String s) {
        return s.capitalize()
    }

    static boolean isAllUpperCase(String s) {
        return s == s.toUpperCase()
    }

    static boolean isAllLowerCase(String s) {
        return s == s.toLowerCase()
    }

    static String repeat(String s, int times) {
        return s * times
    }

    static String removePrefix(String s, String prefix) {
        return s.startsWith(prefix) ? s.substring(prefix.length()) : s
    }

    static String removeSuffix(String s, String suffix) {
        return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : s
    }

    static String replace(String s, String oldStr, String newStr) {
        return s.replace(oldStr, newStr)
    }

    static String trim(String s) {
        return s.trim()
    }

    static boolean isBlank(String s) {
        return s.trim().isEmpty()
    }
}
