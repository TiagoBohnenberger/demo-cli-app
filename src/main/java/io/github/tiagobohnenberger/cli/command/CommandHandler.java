package io.github.tiagobohnenberger.cli.command;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.github.tiagobohnenberger.cli.core.CommandExecutor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject}, access = AccessLevel.PRIVATE)
public class CommandHandler {

    private final CommandExecutor commandExecutor;

    public void handle(Integer userInputHandler) {
        commandExecutor.execute(userInputHandler);
    }
}