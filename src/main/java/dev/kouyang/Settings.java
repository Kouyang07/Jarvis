package dev.kouyang;

import javax.sound.sampled.AudioFormat;

public class Settings {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String accessKey = "s670E92ZaWbE2bJvkhIspNyJbF9W7KXf9gCN9i2PIB1afgCk9ZkZ2A==";
    public static class Porcupine {
        public static String[] keywordPaths = {"C:\\Users\\croti\\Desktop\\JarvisData\\Jarvis_en.ppn"};
    }

    public static class Leopard {

    }
    public static class Logging{
        public static String info = ANSI_GREEN + "[INFO] " + ANSI_RESET;
        public static String error = ANSI_RED + "[ERROR] " + ANSI_RESET;
        public static String warning = ANSI_YELLOW + "[WARNING] " + ANSI_RESET;
    }
}
