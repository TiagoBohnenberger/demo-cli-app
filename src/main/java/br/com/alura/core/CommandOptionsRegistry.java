package br.com.alura.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import br.com.alura.command.Command;
import br.com.alura.command.CommandOption;
import br.com.alura.command.UserInputHandler;
import br.com.alura.util.Functions;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Eager(priority = Eager.Priority.HIGHEST)
@ApplicationScoped
public class CommandOptionsRegistry {
    private static final Class<CommandOption> commandClazz = CommandOption.class;

    private final Map<Integer, CommandExecutor> registry = new HashMap<>();
    private final Instance<Command> commands;

    @Inject
    private CommandOptionsRegistry(@Any Instance<Command> commands) {
        this.commands = commands;
        registerCommandBeans();
    }

    public Optional<CommandExecutor> get(UserInputHandler userInputHandler) {
        return Optional.ofNullable(registry.get(userInputHandler.getValue()));
    }

    private void registerCommandBeans() {
        commands.forEach(commandBean -> {
            if (Functions.isClassCommandOption(commandBean)) {
                registerCommandClass(commandBean);
            } else {
                registerCommandMethod(commandBean);
            }
        });

        log.debug("Command options registry created");
    }

    private void registerCommandMethod(Command commandBean) {
        for (Method method : commandBean.getClass().getDeclaredMethods()) {
            CommandOption commandMethod = method.getAnnotation(commandClazz);
            if (commandMethod != null) {
                registry.put(commandMethod.value(), new CommandExecutor(commandBean, method));
            }
        }
    }

    private void registerCommandClass(Command commandBean) {
        registry.put(
                getCommandOptionValue().apply(commandBean),
                new CommandExecutor(commandBean));
    }

    private Function<Object, Integer> getCommandOptionValue() {
        return command -> command.getClass().getAnnotation(commandClazz).value();
    }


    public record CommandExecutor(Command bean, @Nullable Method method) {
        public CommandExecutor(Command bean) {
            this(bean, null);
        }

        public void execute() throws Exception {
            if (method != null) {
                method.setAccessible(true);
                method.invoke(bean);
            } else {
                bean.execute();
            }
        }

        public String getCommandName() {
            if (method != null) {
                return method.getName();
            }
            return bean.getClass().getSimpleName();
        }
    }
}