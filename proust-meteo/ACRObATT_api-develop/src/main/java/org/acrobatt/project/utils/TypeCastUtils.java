package org.acrobatt.project.utils;

public class TypeCastUtils {

    private TypeCastUtils() {}

    /**
     * Returns true if the given String is numeric (uses a numerical regex)
     * @param string the String
     * @return true if it can be parsed as a number
     */
    public static boolean isNumeric(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }
}
