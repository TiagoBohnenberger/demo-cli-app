package io.github.tiagobohnenberger.cli.util;

import java.util.function.Consumer;
import jakarta.annotation.Nullable;

/**
 * @param <T> the result type (can be null)
 */
public interface Result<T> {

    boolean isFailure();

    @Nullable
    Throwable getException();

    default void orElse(SimpleFunction fallbackOperation) {
        if (isFailure()) {
            fallbackOperation.apply();
        }
    }

    @SuppressWarnings("unchecked")
    default <E extends Throwable> void orElse(Consumer<E> fallbackOperation) {
        if (isFailure()) {
            fallbackOperation.accept((E) getException());
        }
    }

    @Nullable
    T orElseInitial();

    @Nullable
    T getResult();
}