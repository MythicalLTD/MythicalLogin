package xyz.mythicalsystems.mythicallogin.Metrics;

import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;

public class Metrics {

    public Metrics() {
        // Initialize the metrics
        Main.logger.info(Main.class.getName(), "Initializing Metrics");
        try {
            
            MinecraftPlugin.PLUGIN_METRICS = new BStats(MinecraftPlugin.getInstance(), MinecraftPlugin.PLUGIN_ID);

            Main.logger.info(Main.class.getName(), "Metrics Initialized");
        } catch (Exception e) {
            Main.logger.error(Main.class.getName(), "Failed to initialize Metrics: " + e.getMessage());
        }
    }
    
}
