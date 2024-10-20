package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class ForceLogoutAll {
    public ForceLogoutAll(ProxiedPlayer player) {
        if (player.hasPermission(Permissions.ADMIN_FORCELOGOUTALL)) {
            UserDataHandler.setAllBlocked();
            player.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("forceLogout.All"))));
        } else {
            player.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
        }
    }
}
