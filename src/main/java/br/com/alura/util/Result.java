package br.com.alura.util;

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

    @Nullable
    T orElseInitial();

    @Nullable
    T getResult();
}