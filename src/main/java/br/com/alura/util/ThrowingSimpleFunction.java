package br.com.alura.util;

@FunctionalInterface
public interface ThrowingSimpleFunction<E extends Exception> {

    void apply() throws E;
}