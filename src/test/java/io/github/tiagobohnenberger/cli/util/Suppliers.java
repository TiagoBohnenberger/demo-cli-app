package io.github.tiagobohnenberger.cli.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Suppliers {

    public static void throwException() throws Throwable {
        throw new Throwable();
    }

    public static Object newObject() {
        return new Object();
    }
}