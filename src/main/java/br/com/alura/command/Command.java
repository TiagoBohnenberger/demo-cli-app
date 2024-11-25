package br.com.alura.command;

public interface Command {

    Command NO_OP_COMMAND = new NoOpCommand();

    CommandResult execute();

    class NoOpCommand implements Command {

        private NoOpCommand() {
        }

        @Override
        public CommandResult execute() {
            return CommandResult.nonExistentCommand();
        }
    }
}