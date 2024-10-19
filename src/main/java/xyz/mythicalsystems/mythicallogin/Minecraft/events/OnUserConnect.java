package xyz.mythicalsystems.mythicallogin.Minecraft.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class OnUserConnect implements Listener { 

    @EventHandler(priority = 127)
    public void onPlayerConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        MinecraftPlugin.TASK_SCHEDULER.runAsync(MinecraftPlugin.getInstance(), () -> {
            // Register the user
            UserDataHandler.registerUser(player);

        });
    }
    
}
