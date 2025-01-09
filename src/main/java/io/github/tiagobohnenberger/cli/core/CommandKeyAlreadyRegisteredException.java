package io.github.tiagobohnenberger.cli.core;

public class CommandKeyAlreadyRegisteredException extends RuntimeException {
    public CommandKeyAlreadyRegisteredException(String msg) {
        super(msg);
    }
}