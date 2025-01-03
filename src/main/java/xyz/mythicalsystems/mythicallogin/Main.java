package xyz.mythicalsystems.mythicallogin;

import java.sql.Connection;

import xyz.mythicalsystems.mythicallogin.Config.Config;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Logger.Logger;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Metrics.Metrics;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.PinLoginCommand;
import xyz.mythicalsystems.mythicallogin.Minecraft.events.OnUserConnect;
import xyz.mythicalsystems.mythicallogin.MySQL.MySQLConnector;

public class Main {
    /**
     * STATIC VARIABLES
     */
    public static Logger logger;
    public static Connection connection;
    public static MySQLConnector mysql;
    public static Bot bot;
    private static Timer connectionChecker;

    /**
     * Settings for the plugin
     */
    public static boolean CONFIG_METRICS_ENABLED;
    public static String CONFIG_DISCORD_TOKEN;
    public static int CONFIG_DISCORD_GUILD;
    public static String CONFIG_DISCORD_INVITE;
    public static boolean CONFIG_DEBUG_ENABLED;

    /**
     * This method is used to start the plugin
     */
    public static void start() {
        // Initialize the logger
        logger = new Logger();
        logger.info(Main.class.getName(), "Starting Initialization");

        // Initialize the plugin
        logger.info(Main.class.getName(), "Initializing Config");
        try {
            Config.init();
            CONFIG_METRICS_ENABLED = Config.getSetting().getBoolean("Metrics.Enabled");
            CONFIG_DISCORD_TOKEN = Config.getSetting().getString("Discord.token");
            CONFIG_DISCORD_GUILD = Config.getSetting().getInt("Discord.guild");
            CONFIG_DISCORD_INVITE = Config.getSetting().getString("Discord.invite");
            CONFIG_DEBUG_ENABLED = Config.getSetting().getBoolean("Debug.Enabled");
            logger.info(Main.class.getName(), "Config Initialized");
        } catch (Exception e) {
            logger.error(Main.class.getName(), "Failed to initialize Config: " + e.getMessage());
        }

        // Initialize the messages
        logger.info(Main.class.getName(), "Initializing Messages");
        try {
            Messages.init();
            logger.info(Main.class.getName(), "Messages Initialized");
        } catch (Exception e) {
            logger.error(Main.class.getName(), "Failed to initialize Messages: " + e.getMessage());
        }

        // Initialize the metrics
        if (CONFIG_METRICS_ENABLED) {
            new Metrics();
        }
        // Initialize the MySQL connection
        try {
            mysql = new MySQLConnector(Config.getSetting().getString("Database.Host"),
                    Config.getSetting().getString("Database.Port"), Config.getSetting().getString("Database.Database"),
                    Config.getSetting().getString("Database.Username"),
                    Config.getSetting().getString("Database.Password"));
            mysql.tryConnection();
            connection = mysql.getHikari().getConnection();
            startConnectionChecker();

        } catch (Exception e) {
            mysql.reconnect();
            logger.error(Main.class.getName(), "Failed to connect to the MySQL server: " + e.getMessage());
            return;
        }

        if (CONFIG_DISCORD_TOKEN != null && !CONFIG_DISCORD_TOKEN.isEmpty()) {
            bot = new Bot();
            bot.start();
        } else {
            logger.warn(Main.class.getName(), "Discord bot token is not set, bot will not start");
            MinecraftPlugin.getInstance().getProxy().stop();
        }
        // Register commands
        logger.info(Main.class.getName(), "Registering commands...");
        try {
            // Register the pin login command
            MinecraftPlugin.PLUGIN_MANAGER.registerCommand(MinecraftPlugin.getInstance(), new PinLoginCommand());
            logger.info(Main.class.getName(), "Commands registered!");
        } catch (Exception e) {
            logger.error(Main.class.getName(), "Failed to register commands: " + e.getMessage());
        }
        checkLicense();
        // Register events
        logger.info(Main.class.getName(), "Registering events...");
        try {
            // Register the event listener
            MinecraftPlugin.PLUGIN_MANAGER.registerListener(MinecraftPlugin.getInstance(), new OnUserConnect());
            logger.info(Main.class.getName(), "Events registered!");
        } catch (Exception e) {
            logger.error(Main.class.getName(), "Failed to register events: " + e.getMessage());
        }

        logger.info(Main.class.getName(), "Initialization Finished");
        logger.info(Main.class.getName(),
                "Plugin took " + (System.currentTimeMillis() - MinecraftPlugin.START_TIME) + "ms to start");
    }

    /**
     * This method is used to check the connection to the MySQL server
     * 
     * @return void
     */
    private static void startConnectionChecker() {
        connectionChecker = new Timer(true);
        connectionChecker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                        logger.warn(Main.class.getName(), "MySQL connection lost, attempting to reconnect...");
                        connection = mysql.getHikari().getConnection();
                        logger.info(Main.class.getName(), "MySQL connection reestablished.");
                    }
                } catch (Exception e) {
                    logger.error(Main.class.getName(), "Failed to reestablish MySQL connection: " + e.getMessage());
                    MinecraftPlugin.getInstance().getProxy().stop();
                }
            }
        }, 0, 300000); // Check every 5 minutes
    }

    /**
     * This method is used to stop the plugin
     */
    public static void stop() {
        mysql.close();
        logger.info(Main.class.getName(), "Plugin Stopped");
    }

    /**
     * This method is used to reload the plugin
     */
    public static void reload() {
        stop();
        start();
    }

    /**
     * License Server
     */
    @SuppressWarnings("null")
    public static void checkLicense() {
        String licenseString = Config.getSetting().getString("PinLogin.LicenseKey");
        if (licenseString == null || licenseString.isEmpty()) {
            logger.error(Main.class.getName(), "License is not set, stopping the plugin");
            MinecraftPlugin.getInstance().getProxy().stop();
        }
        if (licenseString.length() != 36) {
            logger.error(Main.class.getName(), "Invalid license key length, stopping the plugin");
            MinecraftPlugin.getInstance().getProxy().stop();
        }
        String url = "https://api.mythical.systems/mythicallogin/license/" + licenseString + "/info";

        try {
            java.net.InetAddress address = java.net.InetAddress.getByName(new java.net.URL(url).getHost());
            if (!address.isReachable(5000)) {
                logger.error(Main.class.getName(), "License server is offline, still allowing plugin to start");
            }
        } catch (Exception e) {
            logger.error(Main.class.getName(), "Failed to check if license server is reachable: " + e.getMessage());
        }

        int maxRetries = 3;
        int attempts = 0;
        boolean licenseValid = false;

        while (attempts < maxRetries && !licenseValid) {
            try {
                java.net.URL urlObj = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }

                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(sb.toString());
                boolean success = jsonResponse.getBoolean("success");
                if (!success) {
                    throw new RuntimeException("License check failed: " + jsonResponse.getString("message"));
                }

                logger.info(Main.class.getName(), "License check successful: " + jsonResponse.getString("message"));
                licenseValid = true;
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    logger.error(Main.class.getName(),
                            "Failed to check license after " + maxRetries + " attempts: " + e.getMessage());
                    MinecraftPlugin.getInstance().getProxy().stop();
                } else {
                    logger.warn(Main.class.getName(),
                            "License check attempt " + attempts + " failed: " + e.getMessage());
                }
            }
        }
    }
}
