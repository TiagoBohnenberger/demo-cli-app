package io.github.tiagobohnenberger.cli.tryy;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {

    T get() throws E;
}