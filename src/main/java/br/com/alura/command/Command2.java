package br.com.alura.command;

import jakarta.enterprise.context.ApplicationScoped;

@CommandOption(2)
@ApplicationScoped
public class Command2 implements Command {

    public Command2() {
    }

    @Override
    public CommandResult execute() {
        System.out.println("Comando 2");
        return null;
    }
}