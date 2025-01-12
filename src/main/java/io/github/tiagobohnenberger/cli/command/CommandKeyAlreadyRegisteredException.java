package io.github.tiagobohnenberger.cli.command;

public class CommandKeyAlreadyRegisteredException extends RuntimeException {
    public CommandKeyAlreadyRegisteredException(String msg) {
        super(msg);
    }
}