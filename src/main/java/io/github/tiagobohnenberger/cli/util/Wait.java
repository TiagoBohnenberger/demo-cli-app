package io.github.tiagobohnenberger.cli.util;

import java.util.Objects;

public final class Wait {
    private final Conditional conditional;

    public Wait(Conditional conditional) {
        this.conditional = Objects.requireNonNull(conditional, "\"conditional\" object should never be null");

        if (conditional.getClass().isSynthetic()) {
            throw new Error("Conditional class should not be synthetic");
        }
    }

    public void ifNecessary() {
        synchronized (conditional) {
            while (conditional.satisfy()) {
                try {
                    conditional.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void signalAll() {
        synchronized (conditional) {
            conditional.notifyAll();
        }
    }
}