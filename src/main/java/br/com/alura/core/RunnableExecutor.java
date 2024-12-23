package br.com.alura.core;

import java.util.concurrent.ThreadFactory;

public class RunnableExecutor {
    private volatile static RunnableExecutor INSTANCE;

    private final ThreadFactory threadFactory;

    private RunnableExecutor(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public static RunnableExecutor instance() {
        var instance = INSTANCE;
        if (instance == null) {
            synchronized (Runnable.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = INSTANCE = new RunnableExecutor(UserThreadFactory.instance());
                }
            }
        }
        return instance;
    }

    public void execute(Runnable target) {
        threadFactory.newThread(target).start();
    }
}