package fr.epita.assistants.ping.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static final String RESET_TEXT = "\u001B[0m";
    private static final String RED_TEXT = "\u001B[31m";
    private static final String GREEN_TEXT = "\u001B[32m";
    private static final String YELLOW_TEXT = "\u001B[33m";
    private static final String BLUE_TEXT = "\u001B[34m";

    private static String timestamp() {
        return new SimpleDateFormat("dd/MM/yy - HH:mm:ss")
                .format(Calendar.getInstance().getTime());
    }
    
    public static void info(String message) {
        System.out.println(BLUE_TEXT + "[INFO] " + timestamp() + " - " + message + RESET_TEXT);
    }
    
    public static void success(String message) {
        System.out.println(GREEN_TEXT + "[SUCCESS] " + timestamp() + " - " + message + RESET_TEXT);
    }
    
    public static void error(String message) {
        System.err.println(RED_TEXT + "[ERROR] " + timestamp() + " - " + message + RESET_TEXT);
    }
    
    public static void warning(String message) {
        System.out.println(YELLOW_TEXT + "[WARNING] " + timestamp() + " - " + message + RESET_TEXT);
    }
    
    public static void debug(String message) {
        System.out.println("[DEBUG] " + timestamp() + " - " + message);
    }
    
    public static void jwt(String message) {
        System.out.println(BLUE_TEXT + "[JWT] " + timestamp() + " - " + message + RESET_TEXT);
    }
}
