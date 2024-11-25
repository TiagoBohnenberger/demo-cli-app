package br.com.alura.util;

import jakarta.annotation.Nullable;

public interface Result<T> {

    boolean isFailure();

    @Nullable
    Throwable getException();
}