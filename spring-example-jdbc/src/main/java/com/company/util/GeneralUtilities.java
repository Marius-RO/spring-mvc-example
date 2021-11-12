package com.company.util;

/**
 * Utility class
 */
public final class GeneralUtilities {

    private GeneralUtilities() {
        throw new UnsupportedOperationException("this is an utility class an must not be instantiated");
    }

    public static final String REDIRECT = "redirect:";

    public static char[] trimCharArray(char[] array){
        return String.valueOf(array).trim().toCharArray();
    }
}
