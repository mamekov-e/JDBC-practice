package org.example.utils;

import org.example.logging.Logger;

public class LoggerUtil {

    public static void log(String message) {
        Logger.getInstance().info(message);
    }
}
