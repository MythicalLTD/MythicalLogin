package xyz.mythicalsystems.mythicallogin.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.api.ChatColor;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;

public class Logger {
    /**
     * Constructor for the Logger class
     */
    public Logger() {
        String pluginPath = MinecraftPlugin.PLUGIN_FOLDER_PATH;
        File directory = new File(pluginPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!new File(pluginPath + "/logs").exists()) {
            new File(pluginPath + "/logs").mkdir();
        }

        File logFile = new File(pluginPath + "/logs/log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                MinecraftPlugin.getInstance().getLogger().severe("(Logger) Failed to create log file: " + e.getMessage());
            }
        }

        if (logFile.length() > 1024 * 1024 * 5) {
            File backupFile = new File(pluginPath + "/logs/log_" + System.currentTimeMillis() + ".txt");
            logFile.renameTo(backupFile);
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                MinecraftPlugin.getInstance().getLogger().severe("(Logger) Failed to create log file: " + e.getMessage());
            }
        }
        if (logFile.length() == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
            String formattedDate = dateFormat.format(new Date());
            String logMessage = "[" + formattedDate + "] INFO (Logger): Log file created";

            MinecraftPlugin.getInstance().getLogger().info("Log file created");
            writeLog(logMessage);
        }
    }

    /**
     * Log an error message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void error(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] ERROR (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().severe(message);
        writeLog(logMessage);
    }

    /**
     * Log an info message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void info(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] INFO (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().info(message);
        writeLog(logMessage);
    }

    /**
     * Log a warning message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void warn(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] WARN (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().warning(message);
        writeLog(logMessage);
    }

    /**
     * Log a debug message
     * 
     * @param class_name The name of the class
     * @param message    The message to log
     */
    public void debug(String class_name, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] INFO (" + class_name + "): " + message;

        MinecraftPlugin.getInstance().getLogger().info(message);
        writeLog(logMessage);
    }

    /**
     * Write a message to the log file
     * 
     * @param message
     */
    private void writeLog(String message) {
        message = ChatColor.stripColor(message);

        try {
            File logsDir = new File(MinecraftPlugin.PLUGIN_FOLDER_PATH, "logs");
            if (!logsDir.exists()) {
                logsDir.mkdir();
            }

            try (FileWriter writer = new FileWriter(logsDir + "/log.txt", true)) {
                writer.write(message + "\n");
            } catch (IOException e) {
                MinecraftPlugin.getInstance().getLogger().severe("(Logger) Failed to write log file: " + e.getMessage());
            }
        } catch (Exception e) {
            MinecraftPlugin.getInstance().getLogger().severe("Failed to write to log file");
        }
    }
}
