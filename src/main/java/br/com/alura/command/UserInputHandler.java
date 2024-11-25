package br.com.alura.command;

import java.util.Scanner;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static br.com.alura.util.Functions.print;

@Log4j2
@ApplicationScoped
public class UserInputHandler {

    private final CommandHolder valueHolder;
    @Getter
    private final Scanner scanner;
    private final CommandHandler commandHandler;

    @Inject
    private UserInputHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.scanner = new Scanner(System.in);
        this.valueHolder = new CommandHolder();
    }

    public void startInteraction() {
        while (valueHolder.isNotExit()) {
            print("Selecione sua opção: ");
            readInput();
            if (valueHolder.isExit) break;
            commandHandler.handle(this);
        }
    }

    private void readInput() {
        valueHolder.update(Integer.parseInt(scanner.nextLine()));
    }

    public Integer getValue() {
        return this.valueHolder.value;
    }

    public boolean isExit() {
        return this.valueHolder.value == 5;
    }

    @PreDestroy
    private void preDestroy() {
        this.scanner.close();
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CommandHolder {
        private boolean isExit;
        private int value;

        private boolean isNotExit() {
            return this.value != 5;
        }

        private void update(int value) {
            this.value = value;
            this.isExit = value == 5;
        }
    }
}