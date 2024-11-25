package br.com.alura.command;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import br.com.alura.core.CommandOptionsRegistry;
import br.com.alura.util.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ApplicationScoped
public class CommandHandler {

    private final CommandOptionsRegistry registry;

    @Inject
    private CommandHandler(@Any CommandOptionsRegistry registry) {
        this.registry = registry;
    }

    public void handle(UserInputHandler userInputHandler) {
        registry.get(userInputHandler)
                .map(commandExecutor -> Try.of(commandExecutor::execute))
                .ifPresentOrElse(result -> {
                    if (result.isFailure()) {
                        log.error("Erro ao executar comando \"{}\"",
                                userInputHandler.getValue(),
                                result.getException());
                    }
                }, () -> log.error("Não existe comando registrado para a opção \"{}\"",
                        userInputHandler.getValue()));
    }
}