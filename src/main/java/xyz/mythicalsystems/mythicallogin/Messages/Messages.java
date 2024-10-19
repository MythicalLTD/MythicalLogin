package xyz.mythicalsystems.mythicallogin.Messages;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;

public class Messages {
    /**
     * 
     * Initialize the config file
     * 
     */
    public static void init() {
        String pluginPath = MinecraftPlugin.PLUGIN_FOLDER_PATH;
        File directory = new File(pluginPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(MinecraftPlugin.getInstance().getDataFolder(), "messages.yml");

        if (!file.exists()) {
            try (InputStream in = MinecraftPlugin.getInstance().getResourceAsStream("messages.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                Main.logger.error("Messages", "Failed to create config file");
            } finally {
                Main.logger.info("Messages", "Config file created");
            }
        }
    }
    /**
     * 
     * Get something from the settings file!
     * 
     * @return Configuration
     * 
     */
    public static Configuration getMessage() {
        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(MinecraftPlugin.getInstance().getDataFolder(), "messages.yml"));
            return configuration;
        } catch (IOException e) {
            Main.logger.error("Messages", "Failed to load messages file");
            return null;
        }
    }
}
