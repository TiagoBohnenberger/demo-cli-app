package br.com.alura.core;

import java.io.Closeable;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.alura.util.Conditional;
import br.com.alura.util.Wait;

import static br.com.alura.util.Functions.print;

public class ConsoleReader implements Runnable, Closeable, Conditional {
    private static volatile ConsoleReader INSTANCE;

    private final UserInputHolder userInputHolder;
    private final Scanner scanner;
    private final Wait userInputWait;

    private ConsoleReader(UserInputHolder userInputHolder) {
        this.userInputHolder = userInputHolder;
        this.userInputWait = new Wait(userInputHolder);
        this.scanner = new Scanner(System.in);
    }

    public static ConsoleReader instance() {
        var instance = INSTANCE;
        if (instance == null) {
            synchronized (ConsoleReader.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = INSTANCE = new ConsoleReader(new UserInputHolder());
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        while (userInputHolder.isNotExit()) {
            print("Opção: ");
            int userInput = scanner.nextInt();
            userInputHolder.put(userInput);

            userInputWait.ifNecessary();

            synchronized (this) {
                this.notifyAll();
            }
            System.out.println("Resultado atual -> " + userInput);
        }
    }

    @Override
    public void close() {
        scanner.close();
    }

    public Integer poll() {
        return userInputHolder.poll();
    }

    public boolean isNotExit() {
        return userInputHolder.isNotExit();
    }

    @Override
    public boolean satisfy() {
        return userInputHolder.noResult();
    }

    private static class UserInputHolder implements Conditional {
        final AtomicInteger result = new AtomicInteger(0);

        boolean noResult() {
            return !containsResult();
        }

        boolean containsResult() {
            return result.get() != 0;
        }

        void put(int value) {
            result.set(value);
        }

        boolean isNotExit() {
            return result.get() != 5;
        }

        int poll() {
            return result.getAndSet(0);
        }

        @Override
        public boolean satisfy() {
            return noResult();
        }
    }
}