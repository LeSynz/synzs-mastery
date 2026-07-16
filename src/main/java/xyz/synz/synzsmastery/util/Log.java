package xyz.synz.synzsmastery.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    private Log() {}

    public static final Logger LOGGER = LoggerFactory.getLogger("synzmastery");

    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    public static void error(String message, Object... args) {
        LOGGER.error(message, args);
    }

    public static void debug(String message, Object... args) {
        LOGGER.debug(message, args);
    }

}
