package br.com.alura.core;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import br.com.alura.util.Try;
import lombok.extern.log4j.Log4j2;

@ApplicationScoped
@Log4j2
public class CommandExecutor {

    private final CommandOptionsRegistry registry;

    @Inject
    public CommandExecutor(@Any CommandOptionsRegistry registry) {
        this.registry = registry;
    }

    public void execute(Integer userOption) {
        registry.get(userOption)
                .ifPresentOrElse(
                        command -> Try.of(command::execute),
                        () -> log.error("Não existe comando registrado para a opção \"{}\"", userOption)
                );
    }
}