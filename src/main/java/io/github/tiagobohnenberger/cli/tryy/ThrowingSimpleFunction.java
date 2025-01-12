package io.github.tiagobohnenberger.cli.tryy;

@FunctionalInterface
public interface ThrowingSimpleFunction<E extends Throwable> {

    void apply() throws E;
}