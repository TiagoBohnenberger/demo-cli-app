package br.com.alura.util;

@FunctionalInterface
public interface ThrowingConsumer<E extends Exception> {

    void apply() throws E;
}