package io.github.tiagobohnenberger.cli.util;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {

    T get() throws E;
}