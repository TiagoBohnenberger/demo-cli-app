package br.com.alura.core;

import java.io.Closeable;
import java.util.Scanner;

class ScannerWrapper implements Closeable {
    private final Scanner scanner = new Scanner(System.in);

    private ScannerWrapper() {
    }

    static ScannerWrapper instance() {
        return InstanteHolder.INSTANCE;
    }


    String nextLine() {
        return scanner.nextLine();
    }


    @Override
    public void close() {
        this.scanner.close();
    }

    private static class InstanteHolder {
        private static final ScannerWrapper INSTANCE = new ScannerWrapper();
    }
}