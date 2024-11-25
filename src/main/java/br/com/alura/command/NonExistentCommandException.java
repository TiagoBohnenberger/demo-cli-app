package br.com.alura.command;

public class NonExistentCommandException extends Exception {

    public NonExistentCommandException(String message) {
        super(message);
    }
}