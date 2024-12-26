package br.com.alura.command;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.alura.core.ConsoleReader;
import br.com.alura.core.RunnableExecutor;
import br.com.alura.util.Wait;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ApplicationScoped
public class UserInputHandler implements Runnable {

    private final CommandHandler commandHandler;
    private final ConsoleReader reader;
    private final RunnableExecutor runnableExecutor;
    private final Wait consoleReaderWait;

    @Inject
    private UserInputHandler(CommandHandler commandHandler, ConsoleReader reader) {
        this.commandHandler = commandHandler;
        this.reader = reader;
        this.consoleReaderWait = new Wait(reader);
        this.runnableExecutor = RunnableExecutor.instance();
    }

    public void start() {
        runnableExecutor.executeOnNewThread(this, "INPUT-HANDLER");
        runnableExecutor.executeOnNewThread(reader, "CONSOLE-READER");
    }

    @Override
    public void run() {
        while (reader.isNotExit()) {
            consoleReaderWait.ifNecessary();

            if (reader.isExit()) break;

            commandHandler.handle(reader.poll());

            consoleReaderWait.signalAll();
        }
    }
}