package br.com.alura.core;

import java.util.Scanner;

public class NonblockingBufferedReader {
    private volatile boolean closed = false;
    private Thread backgroundReaderThread;

    private Integer result;

    public NonblockingBufferedReader(final Scanner scanner) {
        backgroundReaderThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    result = scanner.nextInt();
                    synchronized (this) {
                        notifyAll();
                    }
                }
            } finally {
                closed = true;
            }
        }, "Scanner");
        backgroundReaderThread.start();
    }

    public Integer readLine() throws InterruptedException {
        if (closed) {
            return null;
        }
        return result;
    }

    public void close() {
        if (backgroundReaderThread != null) {
            backgroundReaderThread.interrupt();
            backgroundReaderThread = null;
        }
    }

    public boolean empty() {
        return result == null;
    }
}