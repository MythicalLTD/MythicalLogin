package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class UnLink {
    public UnLink(ProxiedPlayer player) {
        if (player.hasPermission(Permissions.USER_UNLINK)) {
            if (UserDataHandler.isDiscordLinked(player)) {
                UserDataHandler.setUserInfo(player, "discord_id", "None");
                UserDataHandler.setUserInfo(player, "discord_pin", "None");
                player.sendMessage(new TextComponent(
                        ChatTranslator.Translate(Messages.getMessage().getString("Discord.Unlink"))));
            } else {
                player.sendMessage(new TextComponent(
                        ChatTranslator.Translate(Messages.getMessage().getString("Discord.NotLinked"))));
            }
        } else {
            player.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
        }
    }
}
