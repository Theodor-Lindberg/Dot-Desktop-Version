package util;

import java.util.logging.Level;

/**
 * The Logger class is just a wrapper for Javas own logging utility.
 */
public final class Logger
{
    private final static java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger(Logger.class.getName());
    }

    private Logger() {

    }

    public static void log(final Level level, final String caller, final String message, final Throwable throwable) {
	final String msg = String.format("[%s] : %s", caller, message);
        LOGGER.log(level, msg, throwable);
    }
}
