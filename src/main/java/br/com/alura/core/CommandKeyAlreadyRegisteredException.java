package br.com.alura.core;

public class CommandKeyAlreadyRegisteredException extends RuntimeException {
    public CommandKeyAlreadyRegisteredException(String msg) {
        super(msg);
    }
}