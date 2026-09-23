package rahulshettyacademy.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtils {

    /**
     * Obtains the logger for the calling class automatically.
     */
    public static Logger getLogger() {
        return LogManager.getLogger(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass());
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}