package io.github.tiagobohnenberger.cli.tryy;

/**
 * @param <T> the type to consume
 * @param <E> them throwable type
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

    void accept(T obj) throws E;
}