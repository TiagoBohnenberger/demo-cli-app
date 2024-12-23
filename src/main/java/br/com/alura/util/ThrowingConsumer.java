package br.com.alura.util;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    void accept(T obj) throws E;
}