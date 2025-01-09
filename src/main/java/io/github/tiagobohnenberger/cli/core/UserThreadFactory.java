package io.github.tiagobohnenberger.cli.core;

import java.util.concurrent.ThreadFactory;
import jakarta.annotation.Nonnull;

public final class UserThreadFactory implements ThreadFactory {

    private UserThreadFactory() {
    }

    public static UserThreadFactory instance() {
        return InstanteHolder.INSTANCE;
    }

    @Override
    public Thread newThread(@Nonnull Runnable target) {
        Thread thread = new Thread(target);
        thread.setDaemon(false);
        return thread;
    }


    private static class InstanteHolder {
        private static final UserThreadFactory INSTANCE = new UserThreadFactory();
    }
}