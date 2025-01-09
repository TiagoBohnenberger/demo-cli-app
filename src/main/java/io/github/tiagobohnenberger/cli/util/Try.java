package io.github.tiagobohnenberger.cli.util;

import java.util.Optional;
import jakarta.annotation.Nullable;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Try<T, E extends Exception> {

    T apply() throws E;

    /** Try to apply a function and wrap the underlying checked exception
     *  into an unchecked exception
     *
     * @param function
     * @param <T>
     * @return the lifted value
     * @throws RuntimeException
     */
    static <T> T lifted(Try<? extends T, ?> function) {
        try {
            return function.apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <E extends Exception> Result<Void> of(ThrowingSimpleFunction<E> supplier) {
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

    static <T> InitiationStep<T> with(T initValue) {
        return new InitiationStep<>(initValue);
    }

    static <T> InitiationStep<T> with(ThrowingSupplier<T, ? extends Exception> initValueSupplier) {
        T value;
        try {
            value = initValueSupplier.get();
        } catch (Exception e) {
            return new InitiationStep<>(e);
        }
        return with(value);
    }


    class Success<T> implements Result<T> {
        @Nullable
        final InitiationStep<T> initStep;

        Success() {
            this.initStep = null;
        }

        Success(T result) {
            this(new InitiationStep<>(result));
        }

        Success(@Nullable InitiationStep<T> initStep) {
            this.initStep = initStep;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public Throwable getException() {
            return Try.with(initStep)
                    .map(initStep -> initStep.exception)
                    .getResult();
        }

        @Override
        public T orElseInitial() {
            if (initStep != null) {
                return initStep.initVal;
            }
            return null;
        }

        @Override
        public T getResult() {
            if (this.initStep != null) {
                return this.initStep.initVal;
            }
            return null;
        }

    }

    class Failure<T> implements Result<T> {
        final InitiationStep<T> initStep;

        Failure(InitiationStep<T> initStep) {
            this.initStep = initStep;
        }

        Failure(Exception e) {
            this.initStep = new InitiationStep<>(e);
        }

        @Override
        public Exception getException() {
            return initStep.exception;
        }

        @Override
        public T orElseInitial() {
            return initStep.initVal;
        }

        @Override
        public T getResult() {
            return initStep.initVal;
        }

        @Override
        public boolean isFailure() {
            return true;
        }
    }

    class InitiationStep<T> {
        @Nullable T initVal;
        @Nullable Exception exception;
        boolean failed;

        InitiationStep(@Nullable T initVal) {
            this.initVal = initVal;
        }

        InitiationStep(@Nullable Exception exception) {
            this.exception = exception;
            this.initVal = null;
            if (exception != null) {
                this.failed = true;
            }
        }

        public <R> Result<R> map(ThrowingFunction<T, R, ? extends Exception> function) {
            if (failed) {
                return new Failure<>(InitiationStep.this.exception);
            }

            try {
                return new Success<>(function.apply(initVal));
            } catch (Exception e) {
                return new Failure<>(e);
            }
        }

        public Result<T> apply(ThrowingConsumer<T, ? extends Exception> consumer) {
            if (failed) {
                return new Failure<>(this);
            }

            try {
                consumer.accept(initVal);
            } catch (Exception e) {
                return new Failure<>(this);
            }
            return new Success<>(this);
        }
    }
}