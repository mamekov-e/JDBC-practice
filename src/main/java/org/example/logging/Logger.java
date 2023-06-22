package org.example.logging;

import org.apache.logging.log4j.LogManager;

public final class Logger {
    private static ThreadLocal<org.apache.logging.log4j.Logger> log4J = ThreadLocal.withInitial(() -> LogManager.getLogger(String.valueOf(Thread.currentThread().getId())));
    private static ThreadLocal<Logger> instance = ThreadLocal.withInitial(Logger::new);

    private Logger() {
    }

    public static Logger getInstance() {
        return instance.get();
    }

    public void debug(String message) {
        (log4J.get()).debug(message);
    }

    public void info(String message) {
        (log4J.get()).info(message);
    }

    public void error(String message) {
        (log4J.get()).error(message);
    }

}
