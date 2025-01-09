package io.github.tiagobohnenberger.cli.core;

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

    public void executeOnNewThread(Runnable target, String threadName) {
        Thread thread = threadFactory.newThread(target);
        if (threadName != null) {
            thread.setName(threadName);
        }
        thread.start();
    }

    public void executeOnNewThread(Runnable target) {
        executeOnNewThread(target, null);
    }
}