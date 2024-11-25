package br.com.alura.command;

import br.com.alura.core.CommandOptionsRegistry.CommandExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

@RequiredArgsConstructor
public class CommandResult implements Message {

    private final Throwable exception;
    private final ParameterizedMessage delegate;
    @Getter
    private final boolean isExit;

    public static CommandResult success(CommandExecutor command) {
        return CommandResult.builder(command).build();
    }

    public static CommandResult error(Throwable ex, CommandExecutor command) {
        return CommandResult.builder(command)
                .exception(ex)
                .build();
    }

    public static CommandResult nonExistentCommand() {
        return CommandResult.builder(new CommandExecutor(Command.NO_OP_COMMAND)).exception(new NonExistentCommandException("Comando não existente")).build();
    }

    public static CommandResult exit() {
        return CommandResult.builder().exit().build();
    }

    private static CommandResultBuilder builder(CommandExecutor command) {
        return new CommandResultBuilder(command);
    }

    private static CommandResultBuilder builder() {
        return builder(null);
    }


    @Override
    public String getFormattedMessage() {
        return delegate.getFormattedMessage();
    }

    @Override
    public Object[] getParameters() {
        return delegate.getParameters();
    }

    @Override
    public Throwable getThrowable() {
        return delegate.getThrowable();
    }

    public boolean isError() {
        return exception != null;
    }

    private static class CommandResultBuilder {
        private Throwable exception;
        private final CommandExecutor commandExecutor;
        private ParameterizedMessage message;
        private boolean exit;

        private CommandResultBuilder(CommandExecutor commandExecutor) {
            this.commandExecutor = commandExecutor;
        }

        public CommandResultBuilder exception(Throwable exception) {
            this.exception = exception;
            this.message = new ParameterizedMessage("Erro ao executar o comando {}. Motivo: {}", new Object[]{commandExecutor.getCommandName(), exception.getLocalizedMessage()}, exception);
            return this;
        }

        public CommandResultBuilder exit() {
            this.exit = true;
            return this;
        }

        public CommandResult build() {
            return new CommandResult(this.exception, this.message, this.exit);
        }
    }
}