package io.github.tiagobohnenberger.cli.tryy;

import java.util.Optional;
import java.util.function.Predicate;

import io.github.tiagobohnenberger.cli.util.Suppliers;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TryTest {

    @Test
    void tryShouldNotThrowAnyException() {
        assertDoesNotThrow(() -> Try.of(Suppliers::throwException));
    }

    @Test
    void whenException_resultShouldBeFailure() {
        var result = Try.of(Suppliers::throwException);

        assertThat(result).is(failure());
    }

    @Test
    void whenNoException_resultShouldBeSuccess() {
        var result = Try.with(Suppliers::newObject)
                .map(ThrowingFunction.identity());

        assertThat(result).is(success());
    }

    @Test
    void whenProvidedFunctionIsNull_shouldReturnFailureWithExplainedCause() {
        Result<Void> result = Try.of(
                (ThrowingSimpleFunction<Throwable>) null);

        assertThat(result)
                .is(failure())
                .extracting(Result::getException).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void whenProvidedFunctionIsNull_shouldReturnFailureWithExplainedCauseV2() {
        Optional<?> result = Try.of(
                (Try<?, ?>) null);

        assertThat(result).isEmpty();
    }


    // --- private methods

    private static Condition<? super Result<Object>> success() {
        Predicate<? super Result<Object>> success = (Result::isFailure);
        success = success.negate();

        return new Condition<>(success, "Success");
    }

    private static Condition<? super Result<?>> failure() {
        return new Condition<>(Result::isFailure, "Failure");
    }
}