package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;

public class Reload {
    public Reload(ProxiedPlayer player) {
        if (player.hasPermission(Permissions.ADMIN_RELOAD)) {
            Main.reload();
            player.sendMessage(new TextComponent(Messages.getMessage().getString("Global.Reloaded").replace("%prefix%", Messages.getMessage().getString("Global.Prefix"))));
        }
    }

    public Reload(CommandSender sender) {
        Main.reload();
        sender.sendMessage(new TextComponent(Messages.getMessage().getString("Global.Reloaded")));
    }
}
