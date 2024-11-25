package br.com.alura.util;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Try<T, E extends Exception> {

    static <T> T lifted(Try<? extends T, ?> function) {
        try {
            return function.apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <E extends Exception> Result<?> of(ThrowingConsumer<E> supplier) {
        requireNonNull(supplier);
        try {
            supplier.apply();
            return new Success<>();
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    static <T> Optional<T> of(Try<? extends T, ? extends Exception> function) {
        requireNonNull(function);
        try {
            return Optional.ofNullable(function.apply());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    T apply() throws E;

    class Success<T> implements Result<T> {

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public Throwable getException() {
            return null;
        }
    }

    class Failure<T> implements Result<T> {

        private final Exception exception;

        public Failure(Exception e) {
            this.exception = e;
        }

        @Override
        public Exception getException() {
            return exception;
        }

        @Override
        public boolean isFailure() {
            return true;
        }
    }
}