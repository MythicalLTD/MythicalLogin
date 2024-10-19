package xyz.mythicalsystems.mythicallogin;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import xyz.mythicalsystems.mythicallogin.Metrics.BStats;


public final class MinecraftPlugin extends Plugin {
    /**
     * The instance of the plugin
     */
    public static MinecraftPlugin instance;

    /**
     * The constructor for the plugin
     */
    public static String PLUGIN_NAME = "MythicalLogin";
    public static String PLUGIN_VERSION = "1.0";
    public static String PLUGIN_AUTHOR = "MythicalSystems, NaysKutzu";
    public static String PLUGIN_DESCRIPTION = "A simple login plugin for BungeeCord";
    public static BStats PLUGIN_METRICS;
    public static String PLUGIN_FOLDER_PATH;
    public static int PLUGIN_ID = 23675;
    public static String OS_NAME;
    public static String OS_VERSION;
    public static String OS_ARCH;
    public static String JAVA_VERSION;
    public static long START_TIME;
    public static PluginManager PLUGIN_MANAGER;
    public static TaskScheduler TASK_SCHEDULER;

    /**
     * Called when the plugin is enabled
     * 
     * This is where the plugin should start
     * 
     * @see Plugin#onEnable()
     * @since 1.0
     * @version 1.0
     * @category Plugin
     * 
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Pre-Initialization started");
        instance = this;
        try {
            PLUGIN_NAME = getDescription().getName();
            PLUGIN_VERSION = getDescription().getVersion();
            PLUGIN_AUTHOR = getDescription().getAuthor();
            PLUGIN_DESCRIPTION = getDescription().getDescription();
            PLUGIN_FOLDER_PATH = getDataFolder().getAbsolutePath();
            JAVA_VERSION = System.getProperty("java.version");
            OS_NAME = System.getProperty("os.name");
            OS_VERSION = System.getProperty("os.version");
            OS_ARCH = System.getProperty("os.arch");
            START_TIME = System.currentTimeMillis();
            PLUGIN_MANAGER = getProxy().getPluginManager();
            TASK_SCHEDULER = getProxy().getScheduler();
            getLogger().info("-------------------------------------");
            getLogger().info("");
            getLogger().info("Starting " + PLUGIN_NAME + " v" + PLUGIN_VERSION + " by " + PLUGIN_AUTHOR);
            getLogger().info("Description: " + PLUGIN_DESCRIPTION);
            getLogger().info("Folder Path: " + PLUGIN_FOLDER_PATH);
            getLogger().info("Java Version: " + JAVA_VERSION);
            getLogger().info("OS Name: " + OS_NAME);
            getLogger().info("OS Version: " + OS_VERSION);
            getLogger().info("OS Architecture: " + OS_ARCH);
            getLogger().info("");
            getLogger().info("-------------------------------------");
        } catch (Exception e) {
            getLogger().info("Error getting plugin information");
        }
        getLogger().info("Pre-Initialization finished");
        Main.start();
    }

    /**
     * Called when the plugin is disabled
     * 
     * This is where the plugin should stop
     * 
     * @see Plugin#onDisable()
     * @since 1.0
     * @version 1.0
     * @category Plugin
     * 
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Main.stop();
    }

    /**
     * Called when the plugin is reloaded
     * 
     * This is where the plugin should reload
     * 
     * @since 1.0
     * @version 1.0
     * @category Plugin
     * 
     */
    public static MinecraftPlugin getInstance() {
        return instance;
    }
}
