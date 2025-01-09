package io.github.tiagobohnenberger.cli.util;

@FunctionalInterface
public interface ThrowingSimpleFunction<E extends Exception> {

    void apply() throws E;
}